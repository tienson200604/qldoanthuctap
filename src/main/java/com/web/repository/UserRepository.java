package com.web.repository;

import com.web.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User,Long>, JpaSpecificationExecutor<User> {

    @Query(value = "select u from User u where u.username = ?1")
    Optional<User> findByUsername(String username);

    @Query(value = "select u from User u where u.email = ?1")
    Optional<User> findByEmail(String email);

    @Query(value = "select u.* from users u where u.id = ?1", nativeQuery = true)
    Optional<User> findById(Long id);

    @Query(value = "select u from User u where u.activation_key = ?1 and u.email = ?2")
    Optional<User> getUserByActivationKeyAndEmail(String key, String email);

    @Query("select u from User u where u.authorities.name = ?2 and (u.email like ?1 or u.fullname like ?1 or u.username like ?1 or u.phone like ?1 )")
    Page<User> getUserByRole(String seearch,String role, Pageable pageable);

    @Query("select u from User u where u.email like ?1 or u.fullname like ?1 or u.phone like ?1 or u.username like ?1")
    Page<User> findAll(String search,Pageable pageable);

    @Query("select count(u.id) from User u where u.authorities.name = ?1")
    public Double countAdmin(String role);

    @Query("select u from User u where u.authorities.name = ?1")
    List<User> getUserByRole(String role);

    @Query("select count(u.id) from User u where u.authorities.name = ?1")
    Long countUserByRole(String role);

    @Query("select u from User u where u.email like ?1 or u.fullname like ?1 or u.username like ?1")
    Set<User> searchByParam(String s);


    @Query(value = "SELECT *\n" +
            "FROM users u\n" +
            "JOIN chat c ON (c.sender = u.id OR c.receiver = u.id)\n" +
            "WHERE (?1 IN (c.sender, c.receiver)) AND u.id != ?1 and u.email like ?2", nativeQuery = true)
    public Set<User> getAllUserChat(Long myUserId, String param);


    @Query(value = "SELECT u.*\n" +
            "FROM users u\n" +
            "LEFT JOIN subject_student st \n" +
            "ON st.user_id = u.id AND st.subject_id = ?1\n" +
            "WHERE st.user_id IS NULL and u.authority_name = 'ROLE_STUDENT' and (u.code like ?2 or u.email like ?2)", nativeQuery = true)
    List<User> userNotJoin(Long subjectId, String param);

    @Query("select u from User u where u.email = ?1 and u.id <> ?2")
    User findByEmailAndId(String email, Long id);

    @Query("select u from User u where lower(u.code) = lower(?1)")
    Optional<User> findByCode(String code);

    @Query("select u from User u where lower(u.code) = lower(?1) and u.id <> ?2")
    Optional<User> findByCode(String code, Long id);
}
