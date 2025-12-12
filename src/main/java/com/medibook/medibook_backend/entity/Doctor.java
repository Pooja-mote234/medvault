package com.medibook.medibook_backend.entity;

import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;
import java.time.LocalDate;

@Entity
@Table(name = "doctor")
public class Doctor implements Persistable<Long> {

    @Id
    private Long id; // Same as user.id (one-to-one FK)

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    // Personal Information
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    private String gender;

    @Column(name = "profile_photo_path")
    private String profilePhotoPath;

    // Professional Information
    @Column(name = "medical_registration_number", unique = true)
    private String medicalRegistrationNumber;

    @Column(name = "licensing_authority")
    private String licensingAuthority;

    private String specialization;

    private String qualification;

    private Integer experience; // years of experience

    // Contact Information
    private String phone;

    // Clinic/Practice Information
    @Column(name = "clinic_hospital_name")
    private String clinicHospitalName;

    private String city;

    private String state;

    private String country;

    private String pincode;

    // Document Uploads
    @Column(name = "medical_license_path")
    private String medicalLicensePath; // Medical license document

    @Column(name = "degree_certificates_path")
    private String degreeCertificatesPath; // Degree certificates

    // Constructors
    public Doctor() {
    }

    public Doctor(User user) {
        this.user = user;
        this.id = user.getId();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            this.id = user.getId();
        }
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfilePhotoPath() {
        return profilePhotoPath;
    }

    public void setProfilePhotoPath(String profilePhotoPath) {
        this.profilePhotoPath = profilePhotoPath;
    }

    public String getMedicalRegistrationNumber() {
        return medicalRegistrationNumber;
    }

    public void setMedicalRegistrationNumber(String medicalRegistrationNumber) {
        this.medicalRegistrationNumber = medicalRegistrationNumber;
    }

    public String getLicensingAuthority() {
        return licensingAuthority;
    }

    public void setLicensingAuthority(String licensingAuthority) {
        this.licensingAuthority = licensingAuthority;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getClinicHospitalName() {
        return clinicHospitalName;
    }

    public void setClinicHospitalName(String clinicHospitalName) {
        this.clinicHospitalName = clinicHospitalName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getMedicalLicensePath() {
        return medicalLicensePath;
    }

    public void setMedicalLicensePath(String medicalLicensePath) {
        this.medicalLicensePath = medicalLicensePath;
    }

    public String getDegreeCertificatesPath() {
        return degreeCertificatesPath;
    }

    public void setDegreeCertificatesPath(String degreeCertificatesPath) {
        this.degreeCertificatesPath = degreeCertificatesPath;
    }

    @Transient
    private boolean isNew = true;

    // Persistable interface methods
    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostLoad
    @PostPersist
    void markNotNew() {
        this.isNew = false;
    }
}
