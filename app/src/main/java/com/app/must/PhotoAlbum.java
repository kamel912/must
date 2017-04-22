package com.app.must;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.app.must.buisness.PhotoAlbumData;
import com.google.android.gms.drive.events.CompletionEvent;
import com.google.android.gms.location.DetectedActivity;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PhotoAlbum extends Activity {
    final ArrayList<PhotoAlbumData> images;

    /* renamed from: com.app.must.PhotoAlbum.2 */
    class AnonymousClass2 implements OnTouchListener {
        private final /* synthetic */ ImageView val$image;
        private final /* synthetic */ int val$imgID;
        private final /* synthetic */ TextView val$subject;

        AnonymousClass2(ImageView imageView, TextView textView, int i) {
            this.val$image = imageView;
            this.val$subject = textView;
            this.val$imgID = i;
        }

        public boolean onTouch(View v, MotionEvent arg1) {
            Iterator it;
            PhotoAlbumData pd;
            switch (arg1.getAction()) {
                case DetectedActivity.IN_VEHICLE /*0*/:
                    int current = Integer.parseInt(this.val$image.getTag().toString());
                    it = PhotoAlbum.this.images.iterator();
                    while (it.hasNext()) {
                        pd = (PhotoAlbumData) it.next();
                        if (pd.getImageID() == current) {
                            Log.e("img", "imgId=" + pd.getImageID() + "row_id=" + pd.getID() + "imagessize=" + PhotoAlbum.this.images.size());
                            int ro;
                            if (pd.getID() == PhotoAlbum.this.images.size()) {
                                ro = ((PhotoAlbumData) PhotoAlbum.this.images.get(0)).getImageID();
                                this.val$image.setImageDrawable(PhotoAlbum.this.getResources().getDrawable(ro));
                                this.val$image.setTag(Integer.valueOf(ro));
                                this.val$subject.setText(((PhotoAlbumData) PhotoAlbum.this.images.get(0)).getSubject().toString());
                            } else {
                                ro = ((PhotoAlbumData) PhotoAlbum.this.images.get(pd.getID())).getImageID();
                                this.val$image.setImageDrawable(PhotoAlbum.this.getResources().getDrawable(ro));
                                this.val$image.setTag(Integer.valueOf(ro));
                                this.val$subject.setText(((PhotoAlbumData) PhotoAlbum.this.images.get(pd.getID())).getSubject().toString());
                            }
                        }
                    }
                    break;
                case CompletionEvent.STATUS_CANCELED /*3*/:
                    it = PhotoAlbum.this.images.iterator();
                    while (it.hasNext()) {
                        pd = (PhotoAlbumData) it.next();
                        if (pd.getImageID() == this.val$imgID && pd.getID() != PhotoAlbum.this.images.size()) {
                            this.val$image.setImageDrawable(PhotoAlbum.this.getResources().getDrawable(((PhotoAlbumData) PhotoAlbum.this.images.get(pd.getID() + 1)).getImageID()));
                        }
                    }
                    break;
            }
            return true;
        }
    }

    private class MyAdapter extends BaseAdapter {
        Context cont;
        private LayoutInflater inflater;
        private List<Item> items;

        private class Item {
            final int drawableId;
            final String name;

            Item(String name, int drawableId) {
                this.name = name;
                this.drawableId = drawableId;
            }
        }

        public MyAdapter(Context context) {
            this.items = new ArrayList();
            this.cont = context;
            this.inflater = LayoutInflater.from(context);
            this.items.add(new Item("", R.drawable.p1));
            this.items.add(new Item("", R.drawable.p2));
            this.items.add(new Item("", R.drawable.p3));
            this.items.add(new Item("", R.drawable.p4));
            this.items.add(new Item("", R.drawable.p5));
            this.items.add(new Item("", R.drawable.p6));
            this.items.add(new Item("", R.drawable.p7));
            this.items.add(new Item("", R.drawable.p8));
            this.items.add(new Item("", R.drawable.p9));
            this.items.add(new Item("", R.drawable.p10));
            this.items.add(new Item("", R.drawable.p11));
            this.items.add(new Item("", R.drawable.p12));
        }

        public int getCount() {
            return this.items.size();
        }

        public Object getItem(int i) {
            return this.items.get(i);
        }

        public long getItemId(int i) {
            return (long) ((Item) this.items.get(i)).drawableId;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = view;
            if (v == null) {
                v = this.inflater.inflate(R.layout.gridview_item, viewGroup, false);
                v.setTag(R.id.picture, v.findViewById(R.id.picture));
                v.setTag(R.id.text, v.findViewById(R.id.text));
            }
            ImageView picture = (ImageView) v.getTag(R.id.picture);
            TextView name = (TextView) v.getTag(R.id.text);
            Item item = (Item) getItem(i);
            Options options = new Options();
            options.inDither = false;
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Config.ARGB_8888;
            options.inSampleSize = 3;
            options.inPurgeable = true;
            picture.setImageBitmap(BitmapFactory.decodeResource(this.cont.getResources(), item.drawableId, options));
            name.setText(item.name);
            picture.setTag(Integer.valueOf(item.drawableId));
            v.setTag(Integer.valueOf(item.drawableId));
            return v;
        }
    }

    public PhotoAlbum() {
        this.images = new ArrayList();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_album);
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient));
        this.images.add(new PhotoAlbumData(1, R.drawable.p1, "Administration Building"));
        this.images.add(new PhotoAlbumData(2, R.drawable.p2, "Mosque"));
        this.images.add(new PhotoAlbumData(3, R.drawable.p3, "Library"));
        this.images.add(new PhotoAlbumData(4, R.drawable.p4, "Medicine"));
        this.images.add(new PhotoAlbumData(5, R.drawable.p5, "Dental Surgery"));
        this.images.add(new PhotoAlbumData(6, R.drawable.p6, "Medicine"));
        this.images.add(new PhotoAlbumData(7, R.drawable.p7, "Languages"));
        this.images.add(new PhotoAlbumData(8, R.drawable.p8, "Celebration"));
        this.images.add(new PhotoAlbumData(9, R.drawable.p9, "Archaeology"));
        this.images.add(new PhotoAlbumData(10, R.drawable.p10, "Applied"));
        this.images.add(new PhotoAlbumData(11, R.drawable.p11, "IT"));
        this.images.add(new PhotoAlbumData(12, R.drawable.p12, "Media"));
        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new MyAdapter(this));
        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Object tid = view.getTag();
                Log.e("Tagid", tid);
                int i = ((Integer) tid).intValue();
                Log.e("Tagid-i", i);
                PhotoAlbum.this.loadPhoto(i);
            }
        });
    }

    private void loadPhoto(int resource) {
        int imgID = resource;
        Builder imageDialog = new Builder(this);
        View layout = ((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.custom_fullimage_dialog, (ViewGroup) findViewById(R.id.layout_root));
        ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
        TextView subject = (TextView) layout.findViewById(R.id.custom_fullimage_placename);
        subject.setText("test");
        image.setImageDrawable(getResources().getDrawable(resource));
        image.setTag(Integer.valueOf(resource));
        image.setOnTouchListener(new AnonymousClass2(image, subject, imgID));
        imageDialog.setView(layout);
        imageDialog.setPositiveButton("Close", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        imageDialog.create();
        imageDialog.show();
    }
}
