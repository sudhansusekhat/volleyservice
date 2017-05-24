package com.sudhansu.videorecord;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by sudhansu on 5/24/2017.
 */

public class Model {

    public  response response;
    public class response {
        public ArrayList<T0> offers;
        public class T0{
            public String storeid;
            public String storename;
        }
        public ArrayList<T1> offer;
        public class T1{
            public String storeid;
            public String storename;
        }
    }

    public Model response(String response){
        return (Model) new Gson().fromJson(response,Model.class);

    }

}
