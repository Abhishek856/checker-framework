package org.checkerframework.checker.tainting;

import java.util.Collection;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.QualifiedNameable;

import org.checkerframework.qualframework.base.AnnotationConverter;

import org.checkerframework.checker.qualparam.QualParams;
import org.checkerframework.checker.qualparam.QualVar;

import org.checkerframework.checker.tainting.qual.Tainted;
import org.checkerframework.checker.tainting.qual.Untainted;
import org.checkerframework.checker.tainting.qual.UseMain;


public class TaintingAnnotationConverter implements AnnotationConverter<QualParams<Tainting>> {
    private String taintedName;
    private String untaintedName;
    private String useMainName;

    public TaintingAnnotationConverter() {
        this.taintedName = Tainted.class.getName();
        this.untaintedName = Untainted.class.getName();
        this.useMainName = UseMain.class.getName();
    }

    private String getAnnotationTypeName(AnnotationMirror anno) {
        Element elt = anno.getAnnotationType().asElement();
        if (elt instanceof QualifiedNameable) {
            QualifiedNameable nameable = (QualifiedNameable)elt;
            return nameable.getQualifiedName().toString();
        } else {
            return null;
        }
    }

    @Override
    public QualParams<Tainting> fromAnnotations(Collection<? extends AnnotationMirror> annos) {
        for (AnnotationMirror anno : annos) {
            String name = getAnnotationTypeName(anno);
            if (taintedName.equals(name)) {
                return new QualParams<>("Main", Tainting.TAINTED);
            } else if (untaintedName.equals(name)) {
                return new QualParams<>("Main", Tainting.UNTAINTED);
            } else if (useMainName.equals(name)) {
                return new QualParams<>("Main", new QualVar<>("Main"));
            }
        }
        return null;
    }

    @Override
    public boolean isAnnotationSupported(AnnotationMirror anno) {
        String name = getAnnotationTypeName(anno);
        return (taintedName.equals(name) || untaintedName.equals(name) || useMainName.equals(name));
    }
}

