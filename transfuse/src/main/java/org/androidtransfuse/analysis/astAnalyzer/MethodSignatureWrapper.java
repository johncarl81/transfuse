package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.util.MethodSignature;
import org.apache.commons.lang.builder.EqualsBuilder;

public class MethodSignatureWrapper {
        private ASTMethod method;
        private MethodSignature methodSignature;

        public MethodSignatureWrapper(ASTMethod method) {
            this.method = method;
            this.methodSignature = new MethodSignature(method);
        }

        public ASTMethod getMethod() {
            return method;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof MethodSignatureWrapper)) {
                return false;
            }

            MethodSignatureWrapper that = (MethodSignatureWrapper) o;

            return new EqualsBuilder().append(methodSignature, that.methodSignature).isEquals();
        }

        @Override
        public int hashCode() {
            return methodSignature.hashCode();
        }
    }