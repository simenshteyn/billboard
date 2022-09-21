package com.robot.billboard.advert;

import com.robot.billboard.security.PersonDetails;
import com.robot.billboard.user.User;
import com.robot.billboard.validator.OnCreate;
import com.robot.billboard.validator.OnUpdate;
import com.robot.billboard.validator.ValidationErrorBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Objects;

@RestController
@Slf4j
@RequestMapping(path = "/adverts")
@Validated
public class AdvertController {
    private final AdvertService advertService;

    @Autowired
    public AdvertController(AdvertService advertService) {
        this.advertService = advertService;
    }

    @GetMapping("/{advertId}")
    public ResponseEntity<?> getAdvertById(@PathVariable @Positive Long advertId) {
        return ResponseEntity.ok(advertService.getAdvertById(advertId));
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyAdverts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        return ResponseEntity.ok(advertService.getAdvertsForUserById(personDetails.getUser().getId()));
    }

    @PostMapping("")
    @Validated(OnCreate.class)
    public ResponseEntity<?> createAdvert(
            HttpServletRequest request,
            @RequestBody @Valid Advert advert,
            Errors errors) {
        if (errors.hasErrors()) {
            log.info("Validation error with request: " + request.getRequestURI());
            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        advert.setOwner(personDetails.getUser());
        return ResponseEntity.ok(advertService.createAdvert(advert));
    }

    @PatchMapping("/{advertId}")
    @Validated(OnUpdate.class)
    public ResponseEntity<?> updateAdvert(
            HttpServletRequest request,
            @RequestBody @Valid Advert advert,
            @PathVariable @Positive Long advertId,
            Errors errors) {
        if (errors.hasErrors()) {
            log.info("Validation error with request: " + request.getRequestURI());
            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
        }
        User advertOwner = advertService.getAdvertById(advertId).getOwner();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        if (!Objects.equals(advertOwner.getId(), personDetails.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No permissions");
        }
        return ResponseEntity.ok(advertService.updateAdvert(advertId, advert));
    }

    @DeleteMapping("/{advertId}")
    public ResponseEntity<?> deleteAdvertById(@PathVariable @Positive Long advertId) {
        User advertOwner = advertService.getAdvertById(advertId).getOwner();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        if (!Objects.equals(advertOwner.getId(), personDetails.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No permissions");
        }
        return ResponseEntity.ok(advertService.deleteAdvertById(advertId));
    }

    @PostMapping("/{advertId}/messages")
    @Validated(OnCreate.class)
    public ResponseEntity<?> messageAdvertById(
            HttpServletRequest request,
            @RequestBody @Valid Message message,
            @PathVariable @Positive Long advertId,
            Errors errors) {
        if (errors.hasErrors()) {
            log.info("Validation error with request: " + request.getRequestURI());
            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
        }
        User advertOwner = advertService.getAdvertById(advertId).getOwner();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        if (Objects.equals(advertOwner.getId(), personDetails.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't message yourself");
        }
        return ResponseEntity.ok(advertService.createAdvertMessage(advertId, personDetails.getUser().getId(), message));
    }

    @GetMapping("/{advertId}/messages")
    public ResponseEntity<?> getAdvertMessagesById(
            @PathVariable @Positive Long advertId) {
        User advertOwner = advertService.getAdvertById(advertId).getOwner();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        if (Objects.equals(advertOwner.getId(), personDetails.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't message yourself");
        }
        return ResponseEntity.ok(advertService.getAdvertMessagesById(advertId, personDetails.getUser().getId()));
    }

    @GetMapping("/{advertId}/reply/{recipientId}")
    public ResponseEntity<?> getMessagesAndRepliesForAdvert(
            @PathVariable @Positive Long advertId,
            @PathVariable @Positive Long recipientId) {
        User advertOwner = advertService.getAdvertById(advertId).getOwner();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        if (!Objects.equals(advertOwner.getId(), personDetails.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only advert owner can see replies");
        }
        return ResponseEntity.ok(advertService.getRepliesForAdvert(advertId, recipientId));
    }

    @GetMapping("/{advertId}/reply/all")
    public ResponseEntity<?> getAllRepliesForAdvert(@PathVariable @Positive Long advertId) {
        User advertOwner = advertService.getAdvertById(advertId).getOwner();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        if (!Objects.equals(advertOwner.getId(), personDetails.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only advert owner can see replies");
        }
        return ResponseEntity.ok(advertService.getAllRepliesForAdvert(advertId));
    }

    @PostMapping("/{advertId}/reply/{recipientId}")
    public ResponseEntity<?> replyToAdvertMessage(
            HttpServletRequest request,
            @RequestBody @Valid Message message,
            @PathVariable @Positive Long advertId,
            @PathVariable @Positive Long recipientId,
            Errors errors
    ) {
        if (errors.hasErrors()) {
            log.info("Validation error with request: " + request.getRequestURI());
            return ResponseEntity.badRequest().body(ValidationErrorBuilder.fromBindingErrors(errors));
        }
        User advertOwner = advertService.getAdvertById(advertId).getOwner();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        if (!Objects.equals(advertOwner.getId(), personDetails.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only advert owner cat reply");
        }
        if (Objects.equals(advertOwner.getId(), recipientId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't reply to yourself");
        }
        return ResponseEntity.ok(advertService.replyToAdvertMessage(advertId, recipientId, message));
    }
}
