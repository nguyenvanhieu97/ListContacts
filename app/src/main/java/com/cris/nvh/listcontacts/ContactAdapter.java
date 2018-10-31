package com.cris.nvh.listcontacts;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
	private ArrayList<Contact> mContacts;
	private OnItemClickListener mListener;

	public ContactAdapter(OnItemClickListener listener, ArrayList<Contact> contacts) {
		mContacts = contacts;
		mListener = listener;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		View view = LayoutInflater
				.from(viewGroup.getContext())
				.inflate(R.layout.view_holder, viewGroup, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
		viewHolder.bindData(mContacts.get(i), i);
	}

	@Override
	public int getItemCount() {
		return mContacts != null ? mContacts.size() : 0;
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		private boolean mIsFavorite;
		private ImageView mImageAvatar;
		private ImageButton mButtonFavorite;
		private ImageButton mButtonCall;
		private TextView mTextName;
		private TextView mTextPhoneNumber;

		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			mImageAvatar = (ImageView) itemView.findViewById(R.id.image_avatar);
			mButtonFavorite = (ImageButton) itemView.findViewById(R.id.button_favorite);
			mButtonCall = (ImageButton) itemView.findViewById(R.id.button_call);
			mTextName = (TextView) itemView.findViewById(R.id.text_name);
			mTextPhoneNumber = (TextView) itemView.findViewById(R.id.text_phone_number);
		}

		public void bindData(final Contact contact, int i) {
			checkFavorite(i);
			mTextName.setText(contact.getName());
			mTextPhoneNumber.setText(contact.getPhoneNumber());
			if (contact.getPhotoUri() == null || contact.getPhotoUri().length() == 0)
				mImageAvatar.setImageResource(R.drawable.human);
			else
				mImageAvatar.setImageURI(Uri.parse(contact.getPhotoUri()));
			setButtonListener(i, contact);
		}

		private void checkFavorite(int i) {
			mIsFavorite = mListener.loadFavorite(i);
			if (mIsFavorite)
				mButtonFavorite.setBackgroundResource(R.color.color_yellow);
			else
				mButtonFavorite.setBackgroundResource(R.color.color_white);
		}

		public void setButtonListener(final int i, final Contact contact) {
			mButtonCall.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					mListener.onItemClick(contact);
				}
			});
			mButtonFavorite.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (!mIsFavorite) {
						mButtonFavorite.setBackgroundResource(R.color.color_yellow);
						mIsFavorite = true;
					} else {
						mButtonFavorite.setBackgroundResource(R.color.color_white);
						mIsFavorite = false;
					}
					mListener.saveFavorite(i, mIsFavorite);
				}
			});
		}
	}

	public interface OnItemClickListener {
		void onItemClick(Contact contact);

		void saveFavorite(int position, boolean isFavorite);

		boolean loadFavorite(int position);
	}
}
