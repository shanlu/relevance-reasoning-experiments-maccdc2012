package edu.neu.ece.concerto.bvrjava;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vistology.bvr.BaseException;
import com.vistology.bvr.LoggerInterface;
import com.vistology.bvr.compile.FunctionRegistry;
import com.vistology.bvr.function.Function;
import com.vistology.bvr.function.FunctionCall;
import com.vistology.bvr.function.FunctionGroup;
import com.vistology.bvr.function.TextContentEnabled;
import com.vistology.bvr.thing.AbbrMap;
import com.vistology.bvr.thing.Thing;
import com.vistology.bvr.thing.ThingFactory;

public class ProceduralAttachments implements FunctionGroup {

	private ThingFactory factory;

	public ProceduralAttachments(ThingFactory factory) {
		this.factory = factory;
	}

	private final static Logger logger = LoggerFactory.getLogger(ProceduralAttachments.class);

	public void registerGroup(FunctionRegistry registry) {
		try {
			registry.register(new MakeAssetFunction(this.factory));
		} catch (BaseException e) {
			logger.error("Failed to register Relevance procedural attachments with BaseVISor.", e);
		}
	}

	class MakeAssetFunction implements Function, TextContentEnabled {

		private final ThingFactory d_factory;
		private final AbbrMap abbrMap;

		public MakeAssetFunction(ThingFactory factory) {
			d_factory = factory;
			this.abbrMap = factory.getAbbreviations();
		}

		public String getName() {
			return "makeAsset";
		}

		public boolean check(LoggerInterface log, FunctionCall call) {
			if (call == null || call.getArgumentCount() < 1) {
				log.error("The " + getName() + " function must have at least one parameter.");
				return false;
			}
			return true;
		}

		public Thing call(Thing[] params, FunctionCall call) {
			String str = message(params, call, abbrMap);
			return d_factory.constructAsset(str);
		}
	}

	private static String message(Thing[] params, FunctionCall call, AbbrMap abbrMap) {

		boolean notrim = false;
		if (call != null) {
			Map<String, String> attrs = call.getAttrs();
			if (attrs != null) {
				String notrimString = attrs.get("notrim");
				if (notrimString != null && notrimString.length() > 0) {
					char notrimChar = notrimString.charAt(0);
					if (notrimChar == 'y' || notrimChar == 't') {
						notrim = true;
					}
				}
			}
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < params.length; ++i) {
			if (params[i] == null) {
				builder.append("null");
			} else {
				builder.append(params[i].toString());
			}
		}
		String text = builder.toString();
		if (!notrim) {
			text = text.trim();
		}

		text = prefixHack(text, abbrMap);

		return text;
	}

	private static String prefixHack(String n, AbbrMap abbrMap) {
		if (StringUtils.isNotBlank(n) && n.contains(":")) {
			String prefix = StringUtils.substringBefore(n, ":");
			String ns = abbrMap.expandGlobalPrefix(prefix);
			if (StringUtils.isNotBlank(ns)) {
				return ns + StringUtils.substringAfter(n, ":");
			}
		}
		return n;
	}

}