package ru.lastgear.httpclient_3.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

import ru.lastgear.httpclient_3.MainActivity;
import ru.lastgear.httpclient_3.R;

/**
 * Created by October on 29.01.2017.
 */

public class CheckBoxFragment extends Fragment implements View.OnClickListener {

    final String LOG_TAG ="qqq";

    public void setmStr(String mStr) {
        this.mStr = mStr;
    }

    public String mStr;
    ListView lv1;
    Button bt_getBalance;
    TextView tv_getBalance;

    public void setTvText(String s){
        tv_getBalance.setText(s);
    }
    public void setTvTextList(List al){

        ArrayAdapter<List> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, al);
        lv1.setAdapter(adapter);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.checkbox_fragment, container, false);

        tv_getBalance = (TextView)view.findViewById(R.id.tv_get_balance);



        bt_getBalance = (Button)view.findViewById(R.id.bt_get_balance);
        bt_getBalance.setOnClickListener(this);


        lv1 = (ListView)view.findViewById(R.id.lv1);

        final String[] catNames = new String[] {
                "Рыжик", "Барсик", "Мурзик", "Мурка", "Васька", "Томасина", "Кристина", "Пушок", "Дымка", "Кузя","Китти", "Масяня", "Симба" };
        // используем адаптер данных
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, catNames);
        lv1.setAdapter(adapter);

        return  view;
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


        switch (v.getId()){
            case R.id.bt_get_balance:




                PostClass pc = new PostClass();

                pc.method = "";
                pc.currencyPair = "";
                pc.rate = 0;
                pc.amount = 0;

                pc.execute(returnBalances);



                break;
        }
    }




    public class PostClass extends AsyncTask<String, Void, String> {


        String method = "returnTicker";
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
            setTvTextList(get_returnBalancesList(s));
            setTvText(get_returnBalances(s));
            switch (method){
                case("Buy"):
                    getBuyString(s, "");
                case("Sell"):
                    getBuyString(s,"");
                case("returnTicker"):
                    get_returnBalances( getReturnTicker(s, "returnTicker"));
            }
        }

        private String doPOSTRequest(String s){
            String mout = "";
            try {
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

                    String hash = MainActivity.bytesToHex(sha_HMAC.doFinal(message.getBytes()));
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

             setmStr(mout);


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

        private String  get_returnBalances (String s) {
           // Log.d("LALALA", jsonArray);
            String parseStr = null;

            try {


                // Код для добычи из GET запроса https://poloniex.com/public?command=returnTicker"
                /*
                String parseStr = "BTC_1CR";
                JSONObject jsonObject = new JSONObject(s);
                jsonObject = jsonObject.getJSONObject(parseStr);
                parseStr = jsonObject.getString("last");*/

                JSONObject jsonObject = new JSONObject(s);
                parseStr = jsonObject.getString("BBR");
            } catch (JSONException e) {
                Log.d(LOG_TAG, "ОШИБКА В  ОБРАБОТКЕ JSON");
                e.printStackTrace();
            }

            return parseStr;
        }

        private ArrayList get_returnBalancesList (String s) {
            Log.e(LOG_TAG , "НАЧАЛО");
            String parseStr = null;
            ArrayList<Valute> arrayList = new ArrayList<Valute>();
            String tmp;

            try {

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = null;
                tmp = jsonObject.toString();

                float f;
                Iterator key = jsonObject.keys();
                while (key.hasNext()) {
                    Valute valute = new Valute();
                    String k = key.next().toString();
                    f = Float.parseFloat(jsonObject.getString(k));
                    if (f > 0) {
                        System.out.println("Key : " + k + ", value : " + jsonObject.getString(k));
                        valute.ValName = k;
                        valute.valValue = jsonObject.getString(k);
                        arrayList.add(valute);
                    }
                }
                //System.out.println( "test " + arrayList.get(2).valValue);

                tmp = jsonObject.toJSONArray( jsonObject.names()).toString();
                //arrayList.add(valute);

               // Log.e("list is:", jsonArray.toString());
                Log.d("Arraylist is:", arrayList.toString());

            } catch (JSONException e) {
                Log.d(LOG_TAG, "ОШИБКА В  ОБРАБОТКЕ JSON");
                e.printStackTrace();
            }

            return arrayList;
        }

        class Valute {
            public String ValName;
            public String valValue;
        }


    }



}
