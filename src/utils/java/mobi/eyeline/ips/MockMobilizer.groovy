#!/usr/bin/env /export/home/user/.groovy-2.2.1/bin/groovy
package mobi.eyeline.ips

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import com.sun.net.httpserver.HttpServer
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.utils.HttpClientUtils
import org.apache.http.impl.client.DefaultHttpClient

import java.util.concurrent.LinkedBlockingQueue

/**
 * Mobilizer-side stub.
 * <br/>
 * Exposes {@code localhost:8000/push} URL.
 * For each pushed message schedules either erroneous or successful notification.
 */
//@Grab(group = 'org.apache.httpcomponents', module = 'httpclient', version = '4.2.4')
class Mock {

  // private static final String SENDER_URL = 'http://192.168.3.209:8080/inform'
  private static final String SENDER_URL = 'http://localhost:8080/inform'
  private static final int N_REPORTING_THREADS = 8

  private final LinkedBlockingQueue<Message> messages = new LinkedBlockingQueue()

  void run() {
    HttpServer.create(new InetSocketAddress(8888), 0).with {
      createContext '/push', new PushHandler()
      setExecutor(null)
      start()
    }
  }

  void runReporters() { (0..<N_REPORTING_THREADS).each { new ReportingThread().start() } }

  private class PushHandler implements HttpHandler {

    @Override
    void handle(HttpExchange xcg) {
      def map = xcg.requestURI.query.split('&').inject([:]) { m, t ->
        t.split('=').with { m[it[0]] = it.length > 1 ? it[1] : '' }; m
      }

      final long resourceId = map['resource_id'].toLong()

      messages << new Message(resourceId)

      xcg.with {
        String response = 'Queued'
        sendResponseHeaders(200, response.length())

        responseBody.with {
          write response.bytes
          close()
        }
      }


    }
  }

  static class Message {
    final long id

    Message(long id) { this.id = id }
  }

  private class ReportingThread extends Thread {

    @Override
    void run() {
      while (!interrupted) {
        def msg = messages.take()
        try {
          boolean isDelivered = msg.id % 2 == 0

          processMessage(msg, 'sms', true, 1, isDelivered)
        } catch (Exception e) {
          println "${msg.id.toString().padRight(8)}" +
              " ${System.currentTimeMillis()}" +
              " ERROR ${e.message}"
        }
      }
    }

    private void processMessage(Message msg,
                                String protocol,
                                boolean isFinal,
                                int status,
                                boolean isDelivered) {

      def deliveredProperty = isDelivered ? "&is-delivered=true" : ""

      def url = "$SENDER_URL?resource_id=${msg.id}&protocol=$protocol&status=$status" + deliveredProperty
      if (isFinal) {
        url += '&final=true'
      }

      final HttpResponse response = new DefaultHttpClient().execute new HttpGet(url)
      println "${msg.id.toString().padRight(8)}" +
          " ${System.currentTimeMillis()}" +
          " ${response.statusLine.statusCode}"

      HttpClientUtils.closeQuietly response
    }
  }

  static void main(String[] args) {
    new Mock().with { srv ->
      Thread.start { srv.run() }
      srv.runReporters()
    }
    println 'Started'
  }
}

Mock.main()
