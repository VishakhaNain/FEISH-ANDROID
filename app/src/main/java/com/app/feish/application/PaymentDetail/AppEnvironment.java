package com.app.feish.application.PaymentDetail;


/**
 * Created by Rahul Hooda on 14/7/17.
 */
public enum AppEnvironment {

    SANDBOX {
        @Override
        public String merchant_Key() {
            return "0EvqkT";
        }

        @Override
        public String merchant_ID() {
            return "4934957";
        }

        @Override
        public String furl() {
            return "https://www.payumoney.com/mobileapp/payumoney/failure.php";
        }

        @Override
        public String surl() {
            return "https://www.payumoney.com/mobileapp/payumoney/success.php";
        }

        @Override
        public String salt() {
            return "5XGCurol";
        }

        @Override
        public String authkey() {
            return "N2ZAm/T1HBhNfboJtJFdi70T2xFA6hKWHrrW3Ca8dlk=";
        }

        @Override
        public boolean debug() {
            return true;
        }
    },
    PRODUCTION {
        @Override
        public String merchant_Key() {
            return "0EvqkT";
        }  //O15vkB

        @Override
        public String merchant_ID() {
            return "4934957";
        }   //4819816

        @Override
        public String furl() {
            return "https://www.payumoney.com/mobileapp/payumoney/failure.php";
        }

        @Override
        public String surl() {
            return "https://www.payumoney.com/mobileapp/payumoney/success.php";
        }

        @Override
        public String salt() {
            return "5XGCurol";
        }     //LU1EhObh

        @Override
        public boolean debug() {
            return false;
        }
        @Override
        public String authkey() {
            return "N2ZAm/T1HBhNfboJtJFdi70T2xFA6hKWHrrW3Ca8dlk=";
        }
    };

    public abstract String merchant_Key();

    public abstract String merchant_ID();

    public abstract String furl();

    public abstract String surl();

    public abstract String salt();
    public abstract String authkey();

    public abstract boolean debug();


}
