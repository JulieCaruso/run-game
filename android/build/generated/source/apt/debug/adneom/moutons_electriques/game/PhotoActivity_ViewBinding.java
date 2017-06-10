// Generated code from Butter Knife. Do not modify!
package adneom.moutons_electriques.game;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PhotoActivity_ViewBinding implements Unbinder {
  private PhotoActivity target;

  @UiThread
  public PhotoActivity_ViewBinding(PhotoActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public PhotoActivity_ViewBinding(PhotoActivity target, View source) {
    this.target = target;

    target.camera = Utils.findRequiredViewAsType(source, R.id.icon_camera, "field 'camera'", ImageView.class);
    target.photo1 = Utils.findRequiredViewAsType(source, R.id.photo1, "field 'photo1'", ImageView.class);
    target.photo2 = Utils.findRequiredViewAsType(source, R.id.photo2, "field 'photo2'", ImageView.class);
    target.enterGame = Utils.findRequiredViewAsType(source, R.id.enter_game, "field 'enterGame'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PhotoActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.camera = null;
    target.photo1 = null;
    target.photo2 = null;
    target.enterGame = null;
  }
}
