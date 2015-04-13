package mobi.eyeline.ips.service;

import mobi.eyeline.ips.external.madv.BannerInfo;
import mobi.eyeline.ips.external.madv.CampaignsSoapImpl;
import mobi.eyeline.ips.external.madv.DeliveryInfo;

import java.util.List;

public class MadvService {

  public int countViews(CampaignsSoapImpl soapApi,
                        int campaignId) {
    int count = 0;

    final List<DeliveryInfo> listDeliveries = soapApi.listDeliveries(campaignId);
    if (listDeliveries != null) {
      for (DeliveryInfo delivery : listDeliveries) {
        count += delivery.getImpressionsCount();
      }
    }

    final List<BannerInfo> listBanners = soapApi.listBanners(campaignId);
    if (listBanners != null) {
      for (BannerInfo banner : listBanners) {
        count += banner.getImpressionsCount();
      }
    }
    return count;
  }
}
