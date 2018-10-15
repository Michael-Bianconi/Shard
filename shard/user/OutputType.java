package shard.user;

public enum OutputType {
    USER_EVENT {
        @Override
        public String prefix() { return "> "; }
    },

    CONVERSATION_QUESTION {
        @Override
        public String prefix() { return "... "; }

    },

    CONVERSATION_RESPONSE {
        @Override
        public String prefix() { return "\""; }
        @Override
        public String suffix() { return "\""; }
    },

    GUEST_EVENT {
        @Override
        public String prefix() { return " * "; }
    },

    DESCRIPTION {
        @Override
        public String prefix() { return "---"; }
    },

    LIST_ITEM {
        @Override
        public String prefix() { return "] "; }
    },

    USER_INPUT_REQUESTED {
        @Override
        public String prefix() { return "$ "; }
    };

    public String prefix() { return ""; }
    public String suffix() { return ""; }
}