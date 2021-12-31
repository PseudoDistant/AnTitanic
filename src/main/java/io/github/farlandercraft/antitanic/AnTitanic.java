package io.github.farlandercraft.antitanic;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class AnTitanic implements ModInitializer {
	public static AnTitanicConfig config = new AnTitanicConfig(true, false, false, 1.0);
	public Gson daData = new GsonBuilder().setPrettyPrinting().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
	Path configPath = Paths.get("config/antitanic.json");

	public void saveDaData() {
		try{
			if (configPath.toFile().exists()) {
				config = daData.fromJson(new String(Files.readAllBytes(configPath)), AnTitanicConfig.class);
			} else {
				Files.write(configPath, Collections.singleton(daData.toJson(config)));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onInitialize() {
		saveDaData();
		System.out.println("No More Shit Boats!");
	}
}
