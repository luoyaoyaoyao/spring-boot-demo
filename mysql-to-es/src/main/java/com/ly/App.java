package com.ly;

import org.apache.http.HttpHost;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.DeprecationHandler;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class App {

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://127.0.0.1:3306/sakila?user=root&password=56774781luoyao&autoReconnect=true&useUnicode=true" +
                "&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }

        Connection con = DriverManager.getConnection(url);
        return con;
    }

    public static void main(String[] args) throws SQLException, IOException {
        Connection connection = getConnection();
//        System.out.println(connection.getMetaData().toString());
        try (RestHighLevelClient client = ESClient.getRestHighLevelClient()) {
            PreparedStatement ps = null;
            String sql = "SELECT * FROM sakila.address";
            int count = 0;
            ResultSet rs;
            BulkRequest bulkRequest = new BulkRequest();
            try {
                ps = connection.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    int adress_id = rs.getInt(1);
                    String adress = rs.getString(2);
                    String adress2 = rs.getString(3);
                    String district = rs.getString(4);
                    int cityId = rs.getInt(5);
                    String postal_code = rs.getString(6);
                    String phone = rs.getString(7);
//                    Date lastUpdate = rs.getDate(9);
                    Map<String, Object> message = new HashMap<>();
                    message.put("adress_id", adress_id);
                    message.put("adress", adress);
                    message.put("adress2", adress2);
                    message.put("district", district);
                    message.put("cityId", cityId);
                    message.put("postal_code", postal_code);
                    message.put("phone", phone);
//                    message.put("lastUpdate", lastUpdate);

                    IndexRequest indexRequest = new IndexRequest("sakila");
                    indexRequest.source(message);
                    bulkRequest.add(indexRequest);

                    System.out.println("write " + (++count));


                }

                BulkResponse bulkItemResponses = client.bulk(bulkRequest, RequestOptions.DEFAULT);


            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
    }
}
