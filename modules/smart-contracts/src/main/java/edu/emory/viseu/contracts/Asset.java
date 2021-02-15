package edu.emory.viseu.contracts;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.json.JSONObject;

@DataType()
public class Asset {

    @Property()
    private String name;

    @Property()
    private String value;

    public String getName(){
        return name;
    }

    public String getValue(){return value;}

    public void setName(String name){
        this.name = name;
    }
    public void setValue(String value){
        this.value = value;
    }
    public String toJSONString(){
        return new JSONObject(this).toString();
    }
    public static Asset fromJSONString(String json){
        String value = new JSONObject(json).getString("value");
        Asset asset = new Asset();
        asset.setValue(value);
        return asset;
    }







}