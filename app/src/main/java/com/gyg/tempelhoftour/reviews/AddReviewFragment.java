package com.gyg.tempelhoftour.reviews;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RatingBar;

import com.gyg.tempelhoftour.R;
import com.gyg.tempelhoftour.data.model.Review;

import java.util.Date;
import java.util.UUID;

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
        void onReviewFinish(Review review);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View rootView = getActivity().getLayoutInflater().inflate(R.layout.fragment_add_review, null);
        ButterKnife.bind(this, rootView);

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(rootView)
                .setCancelable(false)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(onReviewFinish != null && validate()) {
                            Review review = new Review();
                            review.setId(UUID.randomUUID().toString());
                            review.setAuthor(mAuthorEditText.getText().toString());
                            review.setTitle(mTitleEditText.getText().toString());
                            review.setMessage(mMessageEditText.getText().toString());
                            review.setRating(mRating.getRating());
                            review.setDate(new Date());
                            review.setPending(true);
                            onReviewFinish.onReviewFinish(review);
                            dismiss();
                        }
                    }
                })
                .create();

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    private boolean validate() {
        boolean hasError = false;
        if(mAuthorEditText.getText().length() == 0) {
            mAuthorEditText.setError("Voce precisa");
            hasError = true;
        }
        return !hasError;
    }
}
