package ru.odis.address.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//класс обертка для списка анализаторов
@XmlRootElement(name = "analyzers")
public class ListWrapper {

    private List<Analyzer> analyzers;

    @XmlElement(name = "analyzer")
    public List<Analyzer> getAnalyzers() {
        return analyzers;
    }

    public void setAnalyzers(List<Analyzer> analyzers) {
        this.analyzers = analyzers;
    }
}

