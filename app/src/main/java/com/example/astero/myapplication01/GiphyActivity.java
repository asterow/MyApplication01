package com.example.astero.myapplication01;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GiphyActivity extends AppCompatActivity implements HTTPRequestAsyncTask.AsyncResponse, ImageCardGridFragment.OnFragmentInteractionListener {

    private TextView tvGiphyResult;
//    WebView wv;
    private ImageView ivGiphy;
    private ProgressBar pbGiphy;
    private GridView gvGiphy;
    private TextView tvGifUrl;
    private CardView cardViewGiphy;
    private String gifUrl = "";
    private RecyclerView recyclerView;
    private GiphyRecycleViewAdapter giphyRecycleViewAdapter = null;
    private GridLayoutManager gridLayoutManager = null;
    private TinyDB tinyDB;

//    private ImageCardFragment imageCardFragment;
    private ImageCardGridFragment imageCardGridFragment;

    public static ArrayList<String> listGifUrl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giphy);
        tvGiphyResult = (TextView) findViewById(R.id.tvGiphyResult);
        ivGiphy = (ImageView) findViewById(R.id.ivGiphy);
        tvGifUrl = (TextView) findViewById(R.id.tvGiphyResult);
        pbGiphy = (ProgressBar) findViewById(R.id.pbGiphy);
        cardViewGiphy = (CardView) findViewById(R.id.cardViewGiphy);
        pbGiphy.setVisibility(View.GONE);

//        gvGiphy = (GridView) findViewById(R.id.gvGiphy);


//        imageGridListAdapter = new ImageListAdapter(this, listGifUrl);
//        gvGiphy.setAdapter(imageGridListAdapter);
//        gvGiphy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View v,
//                                    int position, long id) {
//                Uri chemin = Uri.parse(imageGridListAdapter.getItem(position).toString());
//                Intent naviguer = new Intent(Intent.ACTION_VIEW, chemin);
//                startActivity(naviguer);
//
//            }
//        });

        listGifUrl = new ArrayList();
        tinyDB = new TinyDB(this);

//        tinyDB.putListString("GIFURLS", listGifUrl);
        listGifUrl = tinyDB.getListString("GIFURLS");
//
//        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        //définit l'agencement des cellules, ici de façon verticale, comme une ListView
//        gridLayoutManager = new GridLayoutManager(this, 2);
//        recyclerView.setLayoutManager(gridLayoutManager);
//
//        giphyRecycleViewAdapter = new GiphyRecycleViewAdapter(listGifUrl);
//        recyclerView.setAdapter(giphyRecycleViewAdapter);
//
//        ItemClickSupport.addTo(recyclerView)
//                .setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
//                    @Override
//                    public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
//                        Uri chemin = Uri.parse(listGifUrl.get(position).toString());
//                        Intent naviguer = new Intent(Intent.ACTION_VIEW, chemin);
//                        startActivity(naviguer);
//                        return false;
//                    }
//                });

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        imageCardGridFragment = ImageCardGridFragment.newInstance(2, listGifUrl);
//        imageCardFragment = ImageCardFragment.newInstance(2, listGifUrl);
//        imageCardGridFragment.onAttach(this);
        ft.replace(R.id.fragment, imageCardGridFragment);
        ft.addToBackStack(null);
        ft.commit();
        RelativeLayout.LayoutParams lpp = (RelativeLayout.LayoutParams) cardViewGiphy.getLayoutParams();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            ft.show(imageCardGridFragment);
            cardViewGiphy.setLayoutParams(lpp);

//            ft.addToBackStack(null);
//            ft.commit();
//        ft.add(R.id.fragment, imageCardGridFragment, "TAG_F1");
//        ft.addToBackStack("TAG_F1");

        }
        else {
            ft.hide(imageCardGridFragment);

            cardViewGiphy.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
//        ft.add(R.id.fragment, imageCardGridFragment, "TAG_F1");
//        ft.addToBackStack("TAG_F1");
//            ft.addToBackStack(null);
//            ft.commit();
        }

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRandGif:
                new GiphyTask().execute();
                break;
            case R.id.btnDownloadGif:
                if (!listGifUrl.contains(gifUrl) && gifUrl.length() > 0) {

//                    giphyRecycleViewAdapter.add(0, gifUrl);
//                    gridLayoutManager.scrollToPositionWithOffset(0, 0);

                    imageCardGridFragment.add(0, gifUrl);
                    tinyDB.putListString("GIFURLS", listGifUrl);
//                    imageGridListAdapter.updateData(listGifUrl);
                }
                //new HTTPRequestAsyncTask(this).execute(gifUrl);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void processFinish(String output, String ACTION) {
        Toast.makeText(this, "DOWNLOAD FINISHHHH !!!!", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public class GiphyTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            StringBuilder result = new StringBuilder();

                String server = "http://api.giphy.com";
                String service = "/v1/gifs/random?api_key=dc6zaTOxFJmzC&tag=american+psycho";
                HttpURLConnection connection = null;

                try{
                    URL url = new URL(server + service);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestMethod("GET");
                    connection.connect();
                    int codeResponse = connection.getResponseCode();
                    if( 200 <= codeResponse && codeResponse < 300 ){
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String line = null;
                        while((line = br.readLine()) != null){
                            result.append(line);
                        }
                        br.close();
                    }
                    else
                        System.out.println("else");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    connection.disconnect();
                }
                return result.toString();
        }

        @Override
        protected void onPostExecute(String result) { // Exécuté dans le UIThread

            try {
                JSONObject jobj = new JSONObject(result);
                gifUrl = jobj.getJSONObject("data").get("image_url").toString();
                tvGifUrl.setText(gifUrl);
                pbGiphy.setVisibility(View.VISIBLE);
//                //wv.getSettings().setJavaScriptEnabled(true);
//                wv.setWebChromeClient(new WebChromeClient() {
//                    public void onProgressChanged(WebView view, int progress)
//                    {
//                        //Make the bar disappear after URL is loaded, and changes string to Loading...
//                        setTitle("Loading...");
//
//                        //setProgress(progress * 100); //Make the bar disappear after URL is loaded
//                        pbGiphy.setProgress(progress);
//                        // Return the app name after finish loading
//                        if(progress == 100) {
//                            pbGiphy.setVisibility(View.INVISIBLE);
//                            setTitle(R.string.app_name);
//                        }
//                    }
//                });
//                wv.loadUrl(gifUrl);


                Glide
                        .with(GiphyActivity.this)
                        .load(gifUrl).listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        pbGiphy.setVisibility(View.GONE);
                        return false;
                    }
                })
                        .fitCenter()
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                        .into(ivGiphy);
            } catch (Exception e) {
                e.printStackTrace();
            }
            tvGifUrl.setText(gifUrl);
        }
    }
}
