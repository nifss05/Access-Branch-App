package com.example.accessapp.addMembers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accessapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AddMemberAdapter extends RecyclerView.Adapter<AddMemberAdapter.MemberAdapterView> {

    private List<MemberData> list;
    private Context context;

    public AddMemberAdapter(List<MemberData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MemberAdapterView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.member_list,parent,false);
        return new MemberAdapterView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberAdapterView holder, int position) {

        MemberData item = list.get(position);
        holder.name.setText(item.getName());
        holder.email.setText(item.getEmail());
        holder.post.setText(item.getPost());
        holder.name.setText(item.getName());
        try {
            Picasso.get().load(item.getImg()).into(holder.imageView);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,UpdateMemberActivity.class);
                intent.putExtra("name",item.getName());
                intent.putExtra("email",item.getEmail());
                intent.putExtra("post",item.getPost());
                intent.putExtra("image",item.getImg());
                intent.putExtra("key",item.getKey());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MemberAdapterView extends RecyclerView.ViewHolder {

        private TextView name,email,post;
        private Button update;
        private ImageView imageView;

        public MemberAdapterView(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.member_name);
            email = itemView.findViewById(R.id.member_email);
            post = itemView.findViewById(R.id.member_post);
            update = itemView.findViewById(R.id.member_update);
            imageView = itemView.findViewById(R.id.member_image);


        }
    }
}
