package se.cambio.hackme.service.impl;

import se.cambio.hackme.service.MemberService;
import se.cambio.hackme.domain.Member;
import se.cambio.hackme.repository.MemberRepository;
import se.cambio.hackme.service.dto.MemberDTO;
import se.cambio.hackme.service.mapper.MemberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Member.
 */
@Service
@Transactional
public class MemberServiceImpl implements MemberService{

    private final Logger log = LoggerFactory.getLogger(MemberServiceImpl.class);
    
    @Inject
    private MemberRepository memberRepository;

    @Inject
    private MemberMapper memberMapper;

    /**
     * Save a member.
     *
     * @param memberDTO the entity to save
     * @return the persisted entity
     */
    public MemberDTO save(MemberDTO memberDTO) {
        log.debug("Request to save Member : {}", memberDTO);
        Member member = memberMapper.memberDTOToMember(memberDTO);
        member = memberRepository.save(member);
        MemberDTO result = memberMapper.memberToMemberDTO(member);
        return result;
    }

    /**
     *  Get all the members.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<MemberDTO> findAll() {
        log.debug("Request to get all Members");
        List<MemberDTO> result = memberRepository.findAll().stream()
            .map(memberMapper::memberToMemberDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one member by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public MemberDTO findOne(Long id) {
        log.debug("Request to get Member : {}", id);
        Member member = memberRepository.findOne(id);
        MemberDTO memberDTO = memberMapper.memberToMemberDTO(member);
        return memberDTO;
    }

    /**
     *  Delete the  member by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Member : {}", id);
        memberRepository.delete(id);
    }
}
