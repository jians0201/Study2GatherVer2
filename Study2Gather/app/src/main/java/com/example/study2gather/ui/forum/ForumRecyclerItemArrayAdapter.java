package com.example.study2gather.ui.forum;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study2gather.ForumQuestion;
import com.example.study2gather.R;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.ArrayList;

public class ForumRecyclerItemArrayAdapter extends RecyclerView.Adapter<ForumRecyclerItemArrayAdapter.MyViewHolder> {

    private ArrayList<ForumQuestion> mQns;
    private ForumRecyclerItemArrayAdapter.MyRecyclerViewItemClickListener mItemClickListener;

    public ForumRecyclerItemArrayAdapter(ArrayList<ForumQuestion> questions, ForumRecyclerItemArrayAdapter.MyRecyclerViewItemClickListener itemClickListener) {
        this.mQns = questions;
        this.mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ForumRecyclerItemArrayAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate RecyclerView row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forum_quest_list, parent, false);

        //Create View Holder
        final ForumRecyclerItemArrayAdapter.MyViewHolder myViewHolder = new ForumRecyclerItemArrayAdapter.MyViewHolder(view);

        //Item Clicks
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClicked(mQns.get(myViewHolder.getLayoutPosition()));
            }
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ForumRecyclerItemArrayAdapter.MyViewHolder holder, int position) {

        //Set Qn User Profile Picture
        if (mQns.get(position).getQnProfilePic() != null) {
            Picasso.get().load(mQns.get(position).getQnProfilePic()).into(holder.imageViewQnUserPic);
        } else {
            holder.imageViewQnUserPic.setImageResource(R.drawable.ic_profile_user_24dp);
        }

        //Set Qn Author
        holder.textViewQnAuthor.setText(mQns.get(position).getQuestionAuthor());

        //Set Qn Time
        Timestamp ts = new Timestamp(mQns.get(position).getTimestamp());
        holder.textViewQnTime.setText(String.valueOf(ts));

        //Set no. of answers
        holder.textViewQnNumAns.setText(String.valueOf(mQns.get(position).getAnsCount()));

        //Set Qn Title
        holder.textViewQnTitle.setText(mQns.get(position).getQuestionTitle());

    }

    @Override
    public int getItemCount() {
        return mQns.size();
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
        private ImageView imageViewQnUserPic;
        private TextView textViewQnTitle, textViewQnTime, textViewQnNumAns, textViewQnAuthor;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewQnUserPic = itemView.findViewById(R.id.forumQuestionDetailsAnswerUserProfilePic);
            textViewQnTitle = itemView.findViewById(R.id.forumQuestion);
            textViewQnTime = itemView.findViewById(R.id.forumQuestionAskTime);
            textViewQnNumAns = itemView.findViewById(R.id.forumCommentCount);
            textViewQnAuthor = itemView.findViewById(R.id.forumQuestionAskUser);
        }
    }

    //RecyclerView Click Listener
    public interface MyRecyclerViewItemClickListener {
        void onItemClicked(ForumQuestion qn);
    }
}
