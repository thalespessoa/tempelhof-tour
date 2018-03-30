package com.gyg.tempelhoftour.reviews;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gyg.tempelhoftour.R;
import com.gyg.tempelhoftour.data.model.PendingReview;
import com.gyg.tempelhoftour.data.model.Review;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thalespessoa on 3/29/18.
 */

public class AddReviewFragment extends DialogFragment {

    @BindView(R.id.et_author)
    protected EditText mAuthorEditText;
    @BindView(R.id.et_title)
    protected EditText mTitleEditText;
    @BindView(R.id.et_message)
    protected EditText mMessageEditText;
    @BindView(R.id.rb_rating)
    protected RatingBar mRating;

    OnReviewFinish onReviewFinish;

    public AddReviewFragment setOnReviewFinish(OnReviewFinish onReviewFinish) {
        this.onReviewFinish = onReviewFinish;
        return this;
    }

    public interface OnReviewFinish {
        void onReviewFinish(PendingReview review);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View rootView = getActivity().getLayoutInflater().inflate(R.layout.fragment_add_review, null);
        ButterKnife.bind(this, rootView);

        return new AlertDialog.Builder(getContext())
                .setView(rootView)
                .setCancelable(false)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(onReviewFinish != null) {
                            PendingReview review = new PendingReview();
                            review.setAuthor(mAuthorEditText.getText().toString());
                            review.setTitle(mTitleEditText.getText().toString());
                            review.setMessage(mMessageEditText.getText().toString());
                            review.setRating(mRating.getRating());
                            review.setDate(new Date());
                            onReviewFinish.onReviewFinish(review);
                            dismiss();
                        }
                    }
                })
                .create();
    }
}
