package ru.lastgear.httpclient_3;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamCorruptedException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends FragmentActivity implements View.OnClickListener{
    final String LOG_TAG ="qqq";
    TextView tv2;
    Button bt_get_som;
    public String mStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(this));

        bt_get_som = (Button)findViewById(R.id.get_som);
        tv2 = (TextView)findViewById(R.id.tv2);

        bt_get_som.setOnClickListener(this);



    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++){
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    @Override
    public void onClick(View v) {

        String returnBalances ="returnBalances";
        String returnCompleteBalances ="returnCompleteBalances";
        String returnDepositAddresses ="returnDepositAddresses";
        String returnDepositsWithdrawals ="returnDepositsWithdrawals";
        String returnOpenOrders ="returnOpenOrders";
        String returnTradeHistory ="returnTradeHistory";
        String returnOrderTrades ="returnOrderTrades";

        String buy ="buy";
        String sell ="sell";

        String cancelOrder ="cancelOrder";
        String moveOrder ="moveOrder";

        String withdraw ="withdraw";

        String returnFeeInfo ="returnFeeInfo";
        String returnAvailableAccountBalances ="returnAvailableAccountBalances";
        String returnTradableBalances ="returnTradableBalances";

        String transferBalance ="transferBalance";

        String returnMarginAccountSummary ="returnMarginAccountSummary";

        String marginBuy ="marginBuy";
        String marginSell ="marginSell";

        String getMarginPosition ="getMarginPosition";
        String closeMarginPosition ="closeMarginPosition";

        String createLoanOffer ="createLoanOffer";
        String cancelLoanOffer ="cancelLoanOffer";
        String returnOpenLoanOffers ="returnOpenLoanOffers";
        String returnActiveLoans ="returnActiveLoans";

        String returnLendingHistory ="returnLendingHistory";

        String toggleAutoRenew ="toggleAutoRenew";


        //////////////ТУТ ЗАПУСКАЕМ НАШ КЛАСС С ПАРАМЕТРОМ!
        PostClass pc = new PostClass();

        pc.method = "";
        pc.currencyPair = "";
        pc.rate = 0;
        pc.amount = 0;

        pc.execute(returnBalances);
    }


    public class PostClass extends AsyncTask<String, Void, String> {


        String method = null;
        String currencyPair = null;
        float rate = 0;
        float amount = 0;





        @Override
        public String doInBackground(String... params) {

            switch (method){
                case("Buy"):
                    return doPOSTRequest(buildBuyString(currencyPair, rate, amount));
                case("Sell"):
                    return doPOSTRequest(buildSellString(currencyPair, rate, amount));
                case("returnTicker"):
                    return doGetRequest("returnTicker");
            }
            return doPOSTRequest(params[0]);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(LOG_TAG, s);

            //Вывод в ТВ
           setTextInTV(get_returnBalances(s));

            switch (method){
                case("Buy"):
                     getBuyString(s, "");
                case("Sell"):
                     getBuyString(s,"");
                case("returnTicker"):
                     getReturnTicker(s, "returnTicker");
            }
        }

        private String doPOSTRequest(String s){
            String mout = "";
            try {
                final TextView outputView = (TextView)findViewById(R.id.postOutput);
                URL url = new URL("https://poloniex.com/tradingApi");
                long unixtTime = 1+ System.currentTimeMillis()/1000L;
                HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
                String urlParameters = "command="+ s +"&nonce=" + unixtTime;
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setRequestProperty("Key", "VL4CZSB0-DR2AYXZV-IF2RHWR5-6FDPHA92");

                try {
                    String secret = "84f83d69a60f1a1bba34eebe97f3a7f7251ce7f224f9216fb9f7a7a02843c15716b6e6fef88826ecf04e497a28231230ba89f1bdf4e254b1973330f568a927cf";
                    String message = urlParameters;

                    Mac sha_HMAC = Mac.getInstance("HmacSHA512");

                    SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA512");
                    sha_HMAC.init(secret_key);

                    String hash = bytesToHex(sha_HMAC.doFinal(message.getBytes()));
                    System.out.println(hash);
                    Log.e("String is ", hash);

                    connection.setRequestProperty("Sign", hash);


                } catch ( Exception e) {
                    e.printStackTrace();
                }

                connection.setDoInput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(urlParameters);
                dStream.flush();
                dStream.close();


                int responseCode = connection.getResponseCode();
                final StringBuffer output = new StringBuffer("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
                output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                while ((line = br.readLine()) != null){
                    responseOutput.append(line);
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + responseOutput.toString());
                mout = responseOutput.toString();

/*                MainActivity.this.runOnUiThread(new Runnable () {
                    @Override
                    public void run() {
                        outputView.setText(output.toString());
                    }
                });*/


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return mout;
        }

        private String doGetRequest (String s){

            String ss = null;
            if (s.isEmpty())
                ss = s;
            else ss = "returnTicker";

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            InputStream stream = null;
            StringBuffer buffer = null;

            String resultStr = "";

            try{
                //Старая версия URL url = new URL("https://poloniex.com/public?command=returnTicker");
                URL url = new URL("https://poloniex.com/public?command=" + ss);
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                stream = connection.getInputStream();
                reader = new BufferedReader((new InputStreamReader(stream)));
                buffer = new StringBuffer();

                String line;
                while ((line = reader.readLine()) != null)
                    buffer.append(line);
                resultStr = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultStr;
        }

        protected String getReturnTicker(String s, String parseStr) {

            if (parseStr.isEmpty())
                parseStr = "BTC_BBR";

            try {
                JSONObject jsonObject = new JSONObject(s);
                jsonObject = jsonObject.getJSONObject(parseStr);
                parseStr = jsonObject.getString("last");
            }catch (Exception e){
                e.printStackTrace();
            }

            return parseStr;
        }
        protected String getSellString(String s, String parseStr) {

            if (parseStr.isEmpty())
                parseStr = "BTC_BBR";

            try {
                JSONObject jsonObject = new JSONObject(s);
                jsonObject = jsonObject.getJSONObject(parseStr);
                parseStr = jsonObject.getString("last");
            }catch (Exception e){
                e.printStackTrace();
            }

            return parseStr;
        }
        protected String getBuyString(String s, String parseStr) {

            if (parseStr.isEmpty())
                parseStr = "BTC_BBR";

            try {
                JSONObject jsonObject = new JSONObject(s);
                jsonObject = jsonObject.getJSONObject(parseStr);
                parseStr = jsonObject.getString("last");
            }catch (Exception e){
                e.printStackTrace();
            }

            return parseStr;
        }

        String buildBuyString (String currencyPair, float rate, float amount){
            StringBuffer sb = new StringBuffer();
            sb.append("buy").append("&currencyPair=").append(currencyPair).append("&rate=")
                    .append(rate).append("&amount").append(amount);

            String res = sb.toString();
            return res;
        }
        String buildSellString (String currencyPair, float rate, float amount){
            StringBuffer sb = new StringBuffer();
            sb.append("buy").append("&currencyPair=").append(currencyPair).append("&rate=")
                    .append(rate).append("&amount").append(amount);

            String res = sb.toString();
            return res;
        }

    }

    private String  get_returnBalances (String s) {
        tv2.setText(s);
        Log.d(LOG_TAG, s);
        String parseStr = "ARCH";

        try {


            // Код для добычи из GET запроса https://poloniex.com/public?command=returnTicker"
                /*
                String parseStr = "BTC_1CR";
                JSONObject jsonObject = new JSONObject(s);
                jsonObject = jsonObject.getJSONObject(parseStr);
                parseStr = jsonObject.getString("last");*/

            JSONObject jsonObject = new JSONObject(s);
            parseStr = jsonObject.getString("BTC");


        } catch (JSONException e) {
            Log.d(LOG_TAG, "ОШИБКА В  ОБРАБОТКЕ JSON");
            e.printStackTrace();
        }

        return parseStr;
    }
// выводит строку в текствью
    private void setTextInTV (String s){
        tv2.setText(s);
    }

}
