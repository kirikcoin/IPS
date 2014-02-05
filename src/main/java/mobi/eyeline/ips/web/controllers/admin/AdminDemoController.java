package mobi.eyeline.ips.web.controllers.admin;

import mobi.eyeline.ips.web.controllers.BaseController;
import mobi.eyeline.util.jsf.components.data_table.model.DataTableModel;
import mobi.eyeline.util.jsf.components.data_table.model.DataTableSortOrder;
import mobi.eyeline.util.jsf.components.data_table.model.ModelException;
import mobi.eyeline.util.jsf.components.dynamic_table.model.DynamicTableModel;
import mobi.eyeline.util.jsf.components.dynamic_table.model.DynamicTableRow;

import java.util.*;

/**
 * author: Denis Enenko
 * date: 21.01.2014
 */
public class AdminDemoController extends BaseController
{
  private List<String> dynamicItems;
  private List<String> selectedDynamicItems;

  private String tabsStyles;

  private String search;

  private List<TableItem> admins;


  public AdminDemoController() {
    admins = Arrays.asList(new TableItem("Админ 1", "79130000001", "a1@a.com"),
                           new TableItem("Админ 2", "79130000002", "a2@a.com"),
                           new TableItem("Админ 3", "79130000003", "a3@a.com"));
    dynamicItems = Arrays.asList("Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6", "Item 7", "Item 8", "Item 9");
    selectedDynamicItems = new ArrayList<String>();
    tabsStyles = "tab_active,tab,tab";
  }

  public DataTableModel getTableModel() {
    return new DataTableModel() {
      final List<TableItem> result = new ArrayList<TableItem>();

      public List getRows(int startPos, int count, DataTableSortOrder sortOrder) throws ModelException {
        if(search != null && !search.isEmpty()) {
          String s = search.toLowerCase();
          for(TableItem admin : admins) {
            if(admin.getName().toLowerCase().contains(s))
              result.add(admin);
          }
        }
        else {
          result.addAll(admins);
        }

        if("name".equals(sortOrder.getColumnId())) {
          final int res = sortOrder.isAsc() ? 1 : -1;
          Collections.sort(result, new Comparator<TableItem>() {
            public int compare(TableItem item1, TableItem item2) {
              return res * item1.getName().compareTo(item2.getName());
            }
          });
        }

        return result;
      }

      @Override
      public int getRowsCount() throws ModelException {
        return result.size();
      }
    };
  }

  public String getSearch() {
    return search;
  }

  public void setSearch(String search) {
    this.search = search;
  }

  public class TableItem
  {
    private String name;
    private String phone;
    private String email;

    public TableItem(String name, String phone, String email) {
      this.name = name;
      this.phone = phone;
      this.email = email;
    }

    public String getName() {
      return name;
    }

    public String getPhone() {
      return phone;
    }

    public String getEmail() {
      return email;
    }
  }


  public Date getDate() {
    return new Date();
  }

  public void setDate(Date date) {
  }


  public DynamicTableModel getSelectedDynamicItems() {
    DynamicTableModel m = new DynamicTableModel();
    for(String item : selectedDynamicItems) {
      DynamicTableRow r = new DynamicTableRow();
      r.setValue("item", item);
      m.addRow(r);
    }
    return m;
  }

  public void setSelectedDynamicItems(DynamicTableModel m) {
    if(m == null || m.getRowCount() == 0)
      return;

    selectedDynamicItems.clear();

    for(DynamicTableRow r : m.getRows()) {
      selectedDynamicItems.add((String) r.getValue("item"));
    }
  }

  public List<String> getDynamicItems() {
    return dynamicItems;
  }


  public void selectTab() {
    int selectedId = getParamValue("tab_id").asInteger();
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < 3; ++i) {
      if(sb.length() > 0) sb.append(",");
      if(i == selectedId)
        sb.append("tab_active");
      else
        sb.append("tab");
    }
    tabsStyles = sb.toString();
  }

  public String getTabsStyles() {
    return tabsStyles;
  }
}
