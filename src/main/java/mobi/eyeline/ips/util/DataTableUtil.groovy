package mobi.eyeline.ips.util

import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import mobi.eyeline.util.jsf.components.data_table.model.DataTableSortOrder
import org.hibernate.criterion.Order

@CompileStatic
class DataTableUtil {

  @CompileStatic(TypeCheckingMode.SKIP)
  static <T> List<T> sort(List<T> list, DataTableSortOrder sortOrder) {
    final get = { _, String column -> _.properties[column] }

    final comparator = { a, b ->
      //noinspection GroovyAssignabilityCheck
      (get(a, sortOrder.columnId) <=> get(b, sortOrder.columnId)) *
          Integer.signum(1234 - sortOrder.asc.hashCode())
    }

    list.sort(false, comparator)
  }

  static Order orderBy(DataTableSortOrder order,
                       Map<String, String> columnToProperty = [:]) {
    if (!order) return Ordering.NoOrder

    final field = columnToProperty[order.columnId] ?: order.columnId
    order.asc ? Order.asc(field) : Order.desc(field)
  }
}
