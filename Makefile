EDUPUB_SAMPLES := src/edupub/samples/WCAG-ch1
EDUPUB_SAMPLES_ZIPPED := $(patsubst %,%.epub,$(EDUPUB_SAMPLES))
SAMPLES_ZIPPED := $(patsubst src/%.epub,samples/%.epub,$(EDUPUB_SAMPLES_ZIPPED))
SAMPLES := $(patsubst %.epub,%,$(SAMPLES_ZIPPED))

.PHONY : all publish

all : $(SAMPLES)

publish :
	./publish-samples.sh

$(EDUPUB_SAMPLES) : src/edupub

src/edupub :
	git clone "https://github.com/IDPF/edupub.git" $@

.SECONDARY : $(EDUPUB_SAMPLES_ZIPPED) $(SAMPLES_ZIPPED)

$(SAMPLES) : % : %.epub
	test -z "$$(git status --porcelain $@)"
	rm -rf $@
	mkdir -p $@
	unzip $< -d $@

$(SAMPLES_ZIPPED) : samples/%.epub : src/%.epub
	rm -f $@
	mkdir -p $(dir $@)
	rm -rf tmp/output-dir
	mkdir -p tmp/output-dir
	mvn test -Dsource=$< -Doutput-dir=tmp/output-dir
	test $$(ls -1 tmp/output-dir | wc -l) -eq 1
	test -f tmp/output-dir/*
	mv tmp/output-dir/* $@

$(EDUPUB_SAMPLES_ZIPPED) : %.epub : %
	rm -f $@
	mkdir -p $(dir $@)
	cd $< && zip -0 -X $(CURDIR)/$@ mimetype
	cd $< && zip -r $(CURDIR)/$@ * -x mimetype

.SECONDEXPANSION:
$(EDUPUB_SAMPLES_ZIPPED) : $$(shell find $(EDUPUB_SAMPLES))
