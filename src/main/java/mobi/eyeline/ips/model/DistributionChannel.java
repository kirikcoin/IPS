package mobi.eyeline.ips.model;

/**
 * Канал распространения.
 */
public enum DistributionChannel {

    /**
     * Клиентская база.
     * <br/>
     * Информирование производится путем отправки сообщения некоторому списку абонентов.
     * Сама рассылка осуществляется через внешнюю систему, про которую IPS ничего не знает.
     * Например, это может быть SMS-Информатор.
     */
    CLIENT_BASE("distributionChannels.clientBase"),

    /**
     * Реклама на запросе баланса.
     * <br/>
     * Информирование производится путем размещения рекламы на USSD-сервисе запроса баланса.
     * Размещение осуществляется через MAdv.
     */
    ADVERT_ON_BALANCE("distributionChannels.AdvertOnBalance");

    public static final String PARAM = "channel";

    private final String resourceName;

    private DistributionChannel(String resourceName) {
        this.resourceName = resourceName;
    }

    public String value() {
        return this.toString();
    }

    // TODO: what are these operations? Can it be simplified to embedded enum logic?

    public static DistributionChannel get(String value) {
        return find(value);
    }

    public static DistributionChannel find(String value) {
        if (value == null)
            return null;
        for (DistributionChannel channel : DistributionChannel.values())
            if (channel.value().equals(value))
                return channel;
        return null;
    }

    public String getResourceName() {
        return resourceName;
    }
}
