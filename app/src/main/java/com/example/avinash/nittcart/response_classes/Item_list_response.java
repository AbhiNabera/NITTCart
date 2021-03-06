package com.example.avinash.nittcart.response_classes;

/**
 * Created by AVINASH on 4/9/2017.
 */
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item_list_response {

    @SerializedName("status_code")
    @Expose
    private Integer statusCode;
    @SerializedName("data")
    @Expose
    private List<Item_structure> data = null;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public List<Item_structure> getData() {
        return data;
    }

    public void setData(List<Item_structure> data) {
        this.data = data;
    }

}
