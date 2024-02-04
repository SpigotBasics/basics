package com.github.spigotbasics.modules.%{module-name-lower}%;

import com.github.spigotbasics.core.module.AbstractBasicsModule;
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext;
import org.jetbrains.annotations.NotNull;

public class %{module-name-pascal}%Module extends AbstractBasicsModule {
    public %{module-name-pascal}%Module(@NotNull final ModuleInstantiationContext context) {
        super(context);
    }

    @Override
    public void onEnable() {

    }
}
