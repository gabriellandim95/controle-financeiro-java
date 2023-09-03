package br.com.controlefinanceiro.envers;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserNameRevisionListener implements RevisionListener {
    @Override
    public void newRevision(Object revisionEntity) {
        RevInfo revision = (RevInfo) revisionEntity;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return;
        }
        revision.setUsername(authentication.getName());
    }
}
