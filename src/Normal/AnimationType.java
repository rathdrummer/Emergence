package Normal;

import com.sun.istack.internal.NotNull;
import org.junit.Assert;

/**
 * Created by oskar on 2017-09-13.
 * This classes has some inputs and outputs
 */
public class AnimationType {
    private final AnimationEnum ae;
    private final DirectionEnum de;

    public AnimationType(@NotNull AnimationEnum ae, @NotNull DirectionEnum de) {

        this.ae = ae;
        this.de = de;
    }

    //for non-directed stuff like rocks or exclamation stuff
    public AnimationType(@NotNull AnimationEnum ae) {
        this(ae, DirectionEnum.None);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnimationType that = (AnimationType) o;

        if (ae != that.ae) return false;
        return de == that.de;

    }

    @Override
    public int hashCode() {
        int result = ae != null ? ae.hashCode() : 0;
        result = 31 * result + (de != null ? de.hashCode() : 0);
        return result;
    }
}
