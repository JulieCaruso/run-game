// Generated code from Butter Knife. Do not modify!
package adneom.moutons_electriques.game.adapter;

import adneom.moutons_electriques.game.R;
import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ScoreAdapter$ScoreViewHolder_ViewBinding implements Unbinder {
  private ScoreAdapter.ScoreViewHolder target;

  @UiThread
  public ScoreAdapter$ScoreViewHolder_ViewBinding(ScoreAdapter.ScoreViewHolder target,
      View source) {
    this.target = target;

    target.image = Utils.findRequiredViewAsType(source, R.id.image, "field 'image'", ImageView.class);
    target.name = Utils.findRequiredViewAsType(source, R.id.name, "field 'name'", TextView.class);
    target.score = Utils.findRequiredViewAsType(source, R.id.score, "field 'score'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ScoreAdapter.ScoreViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.image = null;
    target.name = null;
    target.score = null;
  }
}
