package org.example.User;


import org.example.Exceptions.ApiException;
import org.example.Enums.HibernateUtil;
import org.example.Enums.Roles;
import org.example.Enums.Status;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class UserRepository {
    private static final class Holder {
        private static final UserRepository instance = new UserRepository();
    }

    public static UserRepository getInstance() {
        return Holder.instance;
    }

    private UserRepository() {}

    public synchronized void save(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new ApiException(Status.INTERNAL_SERVER_ERROR,"Failed to save user");
        }
    }

    public synchronized void delete(String id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) session.remove(user);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new ApiException(Status.INTERNAL_SERVER_ERROR,"Failed to delete");
        }
    }

    public synchronized void update(User user) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(user);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new ApiException(Status.INTERNAL_SERVER_ERROR,"Failed to update");
        }
    }

    public synchronized Optional<User> findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.createQuery("FROM User WHERE email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResult();
            return Optional.ofNullable(user);
        }
    }

    public synchronized Optional<User> findById(String id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(User.class, id));
        }
    }

    public synchronized Optional<User> findUserByCardNo(long cardNo) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.createQuery("FROM User u WHERE u.visa.cardNo = :cardNo", User.class)
                    .setParameter("cardNo", cardNo)
                    .uniqueResult();
            return Optional.ofNullable(user);
        }
    }

    public synchronized List<User> findByRole(Roles role) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User WHERE role = :role", User.class)
                    .setParameter("role", role)
                    .list();
        }
    }

    public synchronized List<User> searchByName(String keyword) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
           return session.createQuery(
                            """
                            FROM User
                            WHERE LOWER(CONCAT(firstName, ' ', lastName))
                            LIKE LOWER(:kw)
                            """,
                            User.class
                    )
                    .setParameter("kw", "%" + keyword + "%")
                    .list();
        }
    }
    //Search by full name
    public synchronized List<User> searchByFullName(String keyword) {

    String wk = keyword.substring(0, keyword.indexOf(" "));
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User WHERE name LIKE :kw OR firsName LIKE :wk", User.class)
                    .setParameter("kw", "%" + keyword + "%")
                    .setParameter("wk", "%" + wk + "%")
                    .list();
        }
    }


    public synchronized List<User> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User", User.class).list();
        }
    }

    public synchronized boolean existsByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery("SELECT COUNT(*) FROM User WHERE email = :email", Long.class)
                    .setParameter("email", email)
                    .uniqueResult();
            return count != null && count > 0;
        }
    }

    public synchronized Optional<Double> getCreditsById(String userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Double credits = session.createQuery("SELECT credits FROM User WHERE id = :id", Double.class)
                    .setParameter("id", userId)
                    .uniqueResult();
            return Optional.ofNullable(credits);
        }
    }

    public synchronized void updateCredits(String userId, double amount) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            User user = session.get(User.class, userId);
            if (user != null) {
                user.setCredits(user.getCredits() + amount);
                session.merge(user);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new ApiException(Status.INTERNAL_SERVER_ERROR,"Failed to update credits");
        }
    }
}