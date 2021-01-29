package com.example.study2gather.ui.forum;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study2gather.ForumAnswer;
import com.example.study2gather.ForumQuestion;
import com.example.study2gather.R;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.ArrayList;

public class ForumQuestionDetalsRecyclerItemArrayAdapter extends RecyclerView.Adapter<ForumQuestionDetalsRecyclerItemArrayAdapter.MyViewHolder> {

    private ArrayList<ForumAnswer> mAns;
    private ForumQuestionDetalsRecyclerItemArrayAdapter.MyRecyclerViewItemClickListener mItemClickListener;

    public ForumQuestionDetalsRecyclerItemArrayAdapter(ArrayList<ForumAnswer> answers, ForumQuestionDetalsRecyclerItemArrayAdapter.MyRecyclerViewItemClickListener itemClickListener) {
        this.mAns = answers;
        this.mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ForumQuestionDetalsRecyclerItemArrayAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate RecyclerView row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forum_ans_list, parent, false);

        //Create View Holder
        final ForumQuestionDetalsRecyclerItemArrayAdapter.MyViewHolder myViewHolder = new ForumQuestionDetalsRecyclerItemArrayAdapter.MyViewHolder(view);

        //Item Clicks
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClicked(mAns.get(myViewHolder.getLayoutPosition()));
            }
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ForumQuestionDetalsRecyclerItemArrayAdapter.MyViewHolder holder, int position) {

        //Set Ans User Profile Picture
        if (mAns.get(position).getAnswerProfilePic() != null) {
            Picasso.get().load(mAns.get(position).getAnswerProfilePic()).into(holder.imageViewAnsUserPic);
        } else {
            holder.imageViewAnsUserPic.setImageResource(R.drawable.no_image);
        }

        //Set Ans Author
        holder.textViewAnsAuthor.setText(mAns.get(position).getAnswerAuthor());

        //Set Ans Time
        Timestamp ts = new Timestamp(mAns.get(position).getTimestamp());
        holder.textViewAnsTime.setText(String.valueOf(ts));

        //Set Ans Text
        holder.textViewAnsText.setText(String.valueOf(mAns.get(position).getAnswerText()));

    }

    @Override
    public int getItemCount() {
        return mAns.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //RecyclerView View Holder
    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewAnsUserPic;
        private TextView textViewAnsAuthor, textViewAnsTime, textViewAnsText;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewAnsUserPic = itemView.findViewById(R.id.forumQuestionDetailsAnswerUserProfilePic);
            textViewAnsAuthor = itemView.findViewById(R.id.forumQuestionDetailsAnswerAnswerUser);
            textViewAnsTime = itemView.findViewById(R.id.forumQuestionDetailsAnswerTime);
            textViewAnsText = itemView.findViewById(R.id.forumQuestionDetailsAnswerDetails);
        }
    }

    //RecyclerView Click Listener
    public interface MyRecyclerViewItemClickListener {
        void onItemClicked(ForumAnswer ans);
    }
}
