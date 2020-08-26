package com.baobaoloufu.android.yunpay.bean;

public class PriceBean {

    /**
     * priceInfo : {"price":50,"asked_price":20,"oneonone_price":50}
     */

    private PriceInfoBean priceInfo;

    public PriceInfoBean getPriceInfo() {
        return priceInfo;
    }

    public void setPriceInfo(PriceInfoBean priceInfo) {
        this.priceInfo = priceInfo;
    }

    public static class PriceInfoBean {
        /**
         * price : 50.0
         * asked_price : 20.0
         * oneonone_price : 50.0
         */

        private int price;
        private int asked_price;
        private int oneonone_price;

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getAsked_price() {
            return asked_price;
        }

        public void setAsked_price(int asked_price) {
            this.asked_price = asked_price;
        }

        public int getOneonone_price() {
            return oneonone_price;
        }

        public void setOneonone_price(int oneonone_price) {
            this.oneonone_price = oneonone_price;
        }
    }
}
