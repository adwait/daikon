SRC_DIR := .
DEST_DIR := .
SRC_FILES := $(wildcard $(SRC_DIR)/*.java)
OBJ_FILES := $(patsubst $(SRC_DIR)/%.java,$(DEST_DIR)/%.class,$(SRC_FILES))
JFLAGS := -cp ".:../daikon.jar"

all: $(OBJ_FILES)

$(DEST_DIR)/%.class: $(SRC_DIR)/%.java
	javac $(JFLAGS) -d $(DEST_DIR) $<