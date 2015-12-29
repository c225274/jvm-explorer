package com.blogspot.mikelaud.je.ui.program;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class UiProgramModule extends AbstractModule {

	private void configureConst() {
		bindConstant().annotatedWith(UiProgramConst.ProgramTitle.class).to("JVM Explorer");
		bind(Path.class).annotatedWith(UiProgramConst.ProgramIcon.class).toInstance(Paths.get("program.png"));
		//
		bindConstant().annotatedWith(UiProgramConst.ScaleWidth.class).to(4.0d);
		bindConstant().annotatedWith(UiProgramConst.ScaleHeight.class).to(4.0d);
		//
		bindConstant().annotatedWith(UiProgramConst.EmptyHint.class).to("");
		//
		//--------------------------------------------------------------------
		bind(UiProgramConst.class).to(UiProgramConstImpl.class).in(Singleton.class);
	}
	
	@Override
	protected final void configure() {
		configureConst();
		//
		install(new FactoryModuleBuilder()
			.implement(UiProgram.class, UiProgramImpl.class)
			.build(UiProgram.Factory.class));
	}

}
