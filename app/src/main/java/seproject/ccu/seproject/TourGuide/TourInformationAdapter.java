package seproject.ccu.seproject.TourGuide;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import seproject.ccu.seproject.R;

public class TourInformationAdapter extends RecyclerView.Adapter<TourInformationAdapter.ViewHolder> implements View.OnClickListener{

    private List<OneItem> mData;
    private Context mContext;

    public TourInformationAdapter(List<OneItem> data, Context mContext) {
        this.mData = data;
        this.mContext = mContext;
    }

    private OnItemClickListener mOnItemClickListener = null;

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }



    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tour_guide_list_item,viewGroup,false) ;
        ViewHolder vh = new ViewHolder(v) ;
        v.setOnClickListener(this) ;
        return vh ;
    }

    @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        final OneItem item = mData.get(position) ;

        viewHolder.imageView.setImageBitmap( item.getBitmap() );
        viewHolder.txtName.setText( item.getName() ) ;
        viewHolder.txtDescribe.setText( item.getDescribe() ) ;
        viewHolder.itemView.setTag(position);
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return mData.size() ;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView ;
        public TextView txtName ;
        public TextView txtDescribe ;
        public TextView nothing1;
        public TextView nothing3;

        public ViewHolder(View itemView){
            super(itemView) ;

            imageView = itemView.findViewById(R.id.image);
            txtName = itemView.findViewById(R.id.txtName) ;
            nothing1 = itemView.findViewById(R.id.nothing1);
            txtDescribe = itemView.findViewById(R.id.txtDescribe) ;
            nothing3 = itemView.findViewById(R.id.nothing3);
        }
    }
}


