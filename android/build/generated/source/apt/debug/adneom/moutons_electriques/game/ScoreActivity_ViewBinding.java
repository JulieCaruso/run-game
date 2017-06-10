// Generated code from Butter Knife. Do not modify!
package adneom.moutons_electriques.game;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ScoreActivity_ViewBinding implements Unbinder {
  private ScoreActivity target;

  @UiThread
  public ScoreActivity_ViewBinding(ScoreActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ScoreActivity_ViewBinding(ScoreActivity target, View source) {
    this.target = target;

    target.recycler = Utils.findRequiredViewAsType(source, R.id.recycler, "field 'recycler'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ScoreActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recycler = null;
  }
}
