package org.example.Enums;

import org.example.Enums.CardType;
import org.example.Visa;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.example.Enums.HibernateUtil;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public class VisaRepository {
    private static final class Holder {
        private static final VisaRepository instance = new VisaRepository();
    }

    public static VisaRepository getInstance() {
        return Holder.instance;
    }

    private VisaRepository() {}

    public void save(Visa visa) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(visa);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Failed to save", e);
        }
    }

    public void delete(long cardNo) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Visa visa = session.get(Visa.class, cardNo);
            if (visa != null) session.remove(visa);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Failed to delete", e);
        }
    }

    public void update(Visa visa) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(visa);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Failed to update", e);
        }
    }

    public Optional<Visa> findByCardNo(long cardNo) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Visa.class, cardNo));
        }
    }

    public List<Visa> findByCardType(CardType cardType) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Visa WHERE cardType = :type", Visa.class)
                    .setParameter("type", cardType)
                    .list();
        }
    }

    public List<Visa> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Visa", Visa.class).list();
        }
    }

    public boolean existsByCardNo(long cardNo) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery("SELECT COUNT(*) FROM Visa WHERE cardNo = :cardNo", Long.class)
                    .setParameter("cardNo", cardNo)
                    .uniqueResult();
            return count != null && count > 0;
        }
    }

    public boolean validatePassword(long cardNo, int password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery(
                            "SELECT COUNT(*) FROM Visa WHERE cardNo = :cardNo AND password = :password", Long.class)
                    .setParameter("cardNo", cardNo)
                    .setParameter("password", password)
                    .uniqueResult();
            return count != null && count > 0;
        }
    }

    public boolean isExpired(long cardNo) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Visa visa = session.get(Visa.class, cardNo);
            return visa != null && visa.getCardExpiry().isBefore(YearMonth.now());
        }
    }

    public List<Visa> getExpiredCards() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Visa WHERE cardExpiry < :now", Visa.class)
                    .setParameter("now", YearMonth.now())
                    .list();
        }
    }
}