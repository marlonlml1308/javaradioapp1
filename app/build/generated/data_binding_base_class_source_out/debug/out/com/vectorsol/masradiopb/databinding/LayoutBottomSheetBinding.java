// Generated by view binder compiler. Do not edit!
package com.vectorsol.masradiopb.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.vectorsol.masradiopb.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class LayoutBottomSheetBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ConstraintLayout bottomSheetContainer;

  @NonNull
  public final WebView webView1;

  private LayoutBottomSheetBinding(@NonNull ConstraintLayout rootView,
      @NonNull ConstraintLayout bottomSheetContainer, @NonNull WebView webView1) {
    this.rootView = rootView;
    this.bottomSheetContainer = bottomSheetContainer;
    this.webView1 = webView1;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static LayoutBottomSheetBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static LayoutBottomSheetBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.layout_bottom_sheet, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static LayoutBottomSheetBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      ConstraintLayout bottomSheetContainer = (ConstraintLayout) rootView;

      id = R.id.webView1;
      WebView webView1 = ViewBindings.findChildViewById(rootView, id);
      if (webView1 == null) {
        break missingId;
      }

      return new LayoutBottomSheetBinding((ConstraintLayout) rootView, bottomSheetContainer,
          webView1);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
