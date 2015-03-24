CPP_FILES := $(wildcard src/main/cpp/thrift/*.cpp)
OBJ_FILES :=  $(addprefix target/generated-sources/thrift/,$(notdir $(CPP_FILES:.cpp=.o)))

default: library 

library: $(OBJ_FILES)
	@echo $(THRIFT_HOME)
	@echo $(BOOST_HOME)
	@g++ -shared -L$(THRIFT_HOME)/lib/ -I$(THRIFT_HOME)/include/ -I$(BOOST_HOME)/include/ -I./src/main/cpp/thrift/ -lthrift -DHAVE_NETINET_IN_H -o target/c++/libthrift-adept-api.so $(OBJ_FILES)

target/generated-sources/thrift/%.o: src/main/cpp/thrift/%.cpp
	@g++ -fPIC -L$(THRIFT_HOME)/lib/ -I$(THRIFT_HOME)/include/ -I$(BOOST_HOME)/include/ -I./src/main/cpp/thrift/ -lthrift -DHAVE_NETINET_IN_H -c -o $@ $<

#client: src/test/cpp/examples/CppClient.cpp
#	@g++ -o target/c++/CppClient -L$(THRIFT_HOME)/lib/ -I$(THRIFT_HOME)/include/ -I$(BOOST_HOME)/include/ -I./src/main/cpp/thrift/ -lthrift-0.9.0 -DHAVE_NETINET_IN_H src/test/cpp/examples/CppClient.cpp src/main/cpp/thrift/SentenceProcessor.cpp src/main/cpp/thrift/common_types.cpp
#
#clean:
#	$(RM) -r target/c++/CppClient
#
