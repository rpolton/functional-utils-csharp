package me.shaftesbury.utils.functional.monad;

import me.shaftesbury.utils.functional.IOption;
import me.shaftesbury.utils.functional.MAny;
import me.shaftesbury.utils.functional.Option;

public class MOption<T> implements MAny , IOption{
    private final Option<T> option;

    public MOption(final Option<T> option) {
        this.option=option;
    }
}
