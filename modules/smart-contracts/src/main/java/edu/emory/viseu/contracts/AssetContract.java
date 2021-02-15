package edu.emory.viseu.contracts;

//Code taken and modified from chaincode example in hyperledger documentation
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Transaction;

import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import static java.nio.charset.StandardCharsets.UTF_8;


@Contract(name = "Dummy Contract",
        info = @Info(title = "Dummy contract",
                description = "Very basic Java Contract example",
                version = "0.0.1",
                license =
                @License(name = "SPDX-License-Identifier: Apache-2.0",
                        url = ""),
                contact =  @Contact(email = "MyAssetContract@example.com",
                        name = "MyAssetContract",
                        url = "http://MyAssetContract.me")))

//All smart contracts must implement the ContractInterface interface
@Default
public class AssetContract implements ContractInterface {
    //Anytime the netowrk is interacted with is considered a transaction
    //need to specify this tag everytime a transaction occurs
    @Transaction()
    public boolean assetExists(Context ctx, String assetID){
        byte[] buffer = ctx.getStub().getState(assetID);
        return (buffer != null && buffer.length > 0);
    } //determines if an asset exists in the world state

    @Transaction()
    public void createAsset(Context ctx, String assetID, String value){
        boolean exists = assetExists(ctx, assetID);
        if (exists){
            throw new RuntimeException("The asset " + assetID + " already exists");
        }
        Asset asset = new Asset();
        asset.setValue(value);
        ctx.getStub().putState(assetID, asset.toJSONString().getBytes(UTF_8));

    } //creates a new asset in the world state

    @Transaction()
    public void updateAsset(Context ctx, String assetID, String newVal){
        boolean exists = assetExists(ctx, assetID);
        if (!exists){
            throw new RuntimeException("The asset " + assetID + " does not exist");
        }
        Asset asset = new Asset();
        asset.setValue(newVal);

        ctx.getStub().putState(assetID, asset.toJSONString().getBytes(UTF_8));
    } //updates an existing asset in the worldstate by overwriting the current asset with a new asset

    @Transaction()
    public void deleteAsset(Context ctx, String assetID){
        boolean exists = assetExists(ctx, assetID);
        if (!exists){
            throw new RuntimeException("The asset" + assetID + " does not exist");
        }
        ctx.getStub().delState(assetID);
    } //deletes an existing asset from the world state



}

