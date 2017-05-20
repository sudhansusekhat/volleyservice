package com.sudhansu.testrestapi;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by sudhansu on 5/20/2017.
 */

public class Model {

    public response response;

        public class response{
            public String status;
            public String code;
            public String msg;
            public String totalrecords;

            public ArrayList<offers> offers;

            public class offers{
                public String imgurl;
                public String storeid;
                public String title;
                public String storename;
            }

        }
    public Model response(String response) {
        return (Model) new Gson().fromJson(response,Model.class);
    }

    }

