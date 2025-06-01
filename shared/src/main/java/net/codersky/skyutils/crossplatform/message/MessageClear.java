package net.codersky.skyutils.crossplatform.message;

import org.jetbrains.annotations.NotNull;

public enum MessageClear {

	NONE {
		@Override
		public @NotNull SkyMessageBuilder clearOn(@NotNull SkyMessageBuilder builder) {
			return builder;
		}
	},

	ALL {
		@Override
		public @NotNull SkyMessageBuilder clearOn(@NotNull SkyMessageBuilder builder) {
			FILTERS_AND_EVENTS.clearOn(builder);
			TARGET.clearOn(builder);
			return builder;
		}
	},

	FILTERS {
		@Override
		public @NotNull SkyMessageBuilder clearOn(@NotNull SkyMessageBuilder builder) {
			return builder.clearFilters();
		}
	},

	EVENTS {
		@Override
		public @NotNull SkyMessageBuilder clearOn(@NotNull SkyMessageBuilder builder) {
			return builder.clearFilters();
		}
	},

	FILTERS_AND_EVENTS {
		@Override
		public @NotNull SkyMessageBuilder clearOn(@NotNull SkyMessageBuilder builder) {
			builder.clearFilters();
			return builder.clearEvents();
		}
	},

	TARGET {
		@Override
		public @NotNull SkyMessageBuilder clearOn(@NotNull SkyMessageBuilder builder) {
			return builder.setTarget(MessageTarget.CHAT);
		}
	};

	@NotNull
	public abstract SkyMessageBuilder clearOn(@NotNull final SkyMessageBuilder builder);
}
