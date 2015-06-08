package mobi.eyeline.ips.util

import mobi.eyeline.util.jsf.components.data_table.model.DataTableSortOrder

class DataTableUtil {

  static <T> List<T> sort(List<T> list, DataTableSortOrder sortOrder) {
    final get = { _, String column -> _.properties[column] }

    final comparator = { a, b ->
      (get(a, sortOrder.columnId) <=> get(b, sortOrder.columnId)) *
          Integer.signum(1234 - sortOrder.asc.hashCode())
    }

    list.sort(false, comparator)
  }
}
