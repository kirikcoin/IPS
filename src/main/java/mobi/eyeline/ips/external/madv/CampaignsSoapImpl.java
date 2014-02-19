/**
 * CampaignsSoapImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package mobi.eyeline.ips.external.madv;

public interface CampaignsSoapImpl extends java.rmi.Remote {
    public void init() throws java.rmi.RemoteException;
    public mobi.eyeline.ips.external.madv.DeliveryInfo getDelivery(int campaignId, int deliveryId) throws java.rmi.RemoteException;
    public java.lang.String getCampaignStatus(int campaignId) throws java.rmi.RemoteException;
    public mobi.eyeline.ips.external.madv.CampaignInfo[] listCampaigns() throws java.rmi.RemoteException;
    public mobi.eyeline.ips.external.madv.CampaignInfo addCampaign(mobi.eyeline.ips.external.madv.CampaignParameters campaignParams) throws java.rmi.RemoteException;
    public void updateCampaign(int campaignId, mobi.eyeline.ips.external.madv.CampaignParameters campaignParams) throws java.rmi.RemoteException;
    public boolean removeCampaign(int campaignId) throws java.rmi.RemoteException;
    public mobi.eyeline.ips.external.madv.BannerInfo addTextBanner(int campaignId, mobi.eyeline.ips.external.madv.BannerParams bannerParams) throws java.rmi.RemoteException;
    public boolean removeTextBanner(int campaignId, int bannerId) throws java.rmi.RemoteException;
    public mobi.eyeline.ips.external.madv.BannerInfo getTextBanner(int campaignId, int bannerId) throws java.rmi.RemoteException;
    public mobi.eyeline.ips.external.madv.BannerInfo[] listBanners(int campaignId) throws java.rmi.RemoteException;
    public mobi.eyeline.ips.external.madv.DeliveryInfo addTextDelivery(int campaignId, mobi.eyeline.ips.external.madv.DeliveryParams deliveryParams) throws java.rmi.RemoteException;
    public boolean removeTextDelivery(int campaignId, int deliveryId) throws java.rmi.RemoteException;
    public mobi.eyeline.ips.external.madv.DeliveryInfo[] listDeliveries(int campaignId) throws java.rmi.RemoteException;
    public mobi.eyeline.ips.external.madv.CampaignInfo startCampaign(int campaignId) throws java.rmi.RemoteException;
    public mobi.eyeline.ips.external.madv.CampaignInfo pauseCampaign(int campaignId) throws java.rmi.RemoteException;
    public mobi.eyeline.ips.external.madv.CampaignInfo stopCampaign(int campaignId) throws java.rmi.RemoteException;
    public mobi.eyeline.ips.external.madv.FileInfo getLandingConversionCounterStat(int fromId) throws java.rmi.RemoteException;
    public mobi.eyeline.ips.external.madv.FileInfo getLandingPageClickCounterStat(int fromId) throws java.rmi.RemoteException;
    public mobi.eyeline.ips.external.madv.FileInfo getMessageViewCounterStat(int fromId) throws java.rmi.RemoteException;
    public java.lang.String[] getFreeClickServiceShortcuts() throws java.rmi.RemoteException;
}
