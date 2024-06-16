// src/components/LandingPage.js

import React, { useEffect, useState } from "react";
import BookingForm from "./BookingForm";
import "./LandingPage.css";
import LoginForm from "./LoginForm";
import Modal from "./Modal";

const LandingPage = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalContent, setModalContent] = useState(null);

  const openBookingModal = () => {
    setModalContent(
      <BookingForm
        onSubmit={(data) => console.log("Booking data:", data)}
        onClose={() => setIsModalOpen(false)}
      />
    );
    setIsModalOpen(true);
  };

  const openLoginModal = () => {
    setModalContent(
      <LoginForm
        onSubmit={(data) => {
          console.log("Login data:", data);
          setIsModalOpen(false);
        }}
      />
    );
    setIsModalOpen(true);
  };

  useEffect(() => {
    const handleScroll = () => {
      const nav = document.querySelector(".nav");
      const section1 = document.querySelector("#section--1");
      const navbarOffsetTop = section1.offsetTop;

      if (window.pageYOffset >= navbarOffsetTop) {
        nav.classList.add("sticky");
      } else {
        nav.classList.remove("sticky");
      }
    };

    window.addEventListener("scroll", handleScroll);

    return () => {
      window.removeEventListener("scroll", handleScroll);
    };
  }, []);

  useEffect(() => {
    const allSections = document.querySelectorAll(".section");
    const revealSection = function (entries, observer) {
      const [entry] = entries;
      if (entry.isIntersecting) {
        entry.target.classList.remove("section--hidden");
        observer.unobserve(entry.target);
      }
    };

    const sectionObserver = new IntersectionObserver(revealSection, {
      root: null,
      threshold: 0.2,
    });

    allSections.forEach((section) => {
      sectionObserver.observe(section);
      section.classList.add("section--hidden");
    });

    const imgTargets = document.querySelectorAll("img[data-src]");
    const loadImg = function (entries, observer) {
      const [entry] = entries;
      if (!entry.isIntersecting) return;

      entry.target.src = entry.target.dataset.src;
      entry.target.addEventListener("load", function () {
        entry.target.classList.remove("lazy-img");
      });
      observer.unobserve(entry.target);
    };

    const imgObserver = new IntersectionObserver(loadImg, {
      root: null,
      threshold: 0.2,
      rootMargin: "200px",
    });

    imgTargets.forEach((img) => imgObserver.observe(img));

    const slider = function () {
      const slides = document.querySelectorAll(".slide");
      const btnLeft = document.querySelector(".slider__btn--left");
      const btnRight = document.querySelector(".slider__btn--right");
      const dotContainer = document.querySelector(".dots");

      let curSlide = 0;
      const maxSlide = slides.length;

      const createDots = function () {
        slides.forEach(function (_, i) {
          dotContainer.insertAdjacentHTML(
            "beforeend",
            `<button class="dots__dot" data-slide="${i}"></button>`
          );
        });
      };

      const activateDot = function (slide) {
        document
          .querySelectorAll(".dots__dot")
          .forEach((dot) => dot.classList.remove("dots__dot--active"));

        document
          .querySelector(`.dots__dot[data-slide="${slide}"]`)
          .classList.add("dots__dot--active");
      };

      const goToSlide = function (slide) {
        slides.forEach(
          (s, i) => (s.style.transform = `translateX(${100 * (i - slide)}%)`)
        );
      };

      const nextSlide = function () {
        if (curSlide === maxSlide - 1) {
          curSlide = 0;
        } else {
          curSlide++;
        }
        goToSlide(curSlide);
        activateDot(curSlide);
      };

      const prevSlide = function () {
        if (curSlide === 0) {
          curSlide = maxSlide - 1;
        } else {
          curSlide--;
        }
        goToSlide(curSlide);
        activateDot(curSlide);
      };

      const init = function () {
        goToSlide(0);
        createDots();
        activateDot(0);
      };
      init();

      btnRight.addEventListener("click", nextSlide);
      btnLeft.addEventListener("click", prevSlide);

      document.addEventListener("keydown", function (e) {
        if (e.key === "ArrowLeft") prevSlide();
        if (e.key === "ArrowRight") nextSlide();
      });

      dotContainer.addEventListener("click", function (e) {
        if (e.target.classList.contains("dots__dot")) {
          const { slide } = e.target.dataset;
          goToSlide(slide);
          activateDot(slide);
        }
      });
    };
    slider();
  }, []);

  return (
    <div>
      <header className="header">
        {/* <video className="header__video" autoPlay muted loop>
          <source src="/video/HOSPITAL.mp4" type="video/mp4" />
          Your browser does not support the video tag.
        </video> */}
        <nav className="nav">
          <img
            src="/img/Dampson__5.png"
            alt="Quick credit logo"
            className="nav__logo"
            id="logo"
          />
          <ul className="nav__links">
            <li className="nav__item">
              <a className="nav__link" href="#section--1">
                Features
              </a>
            </li>
            <li className="nav__item">
              <a className="nav__link" href="#section--2">
                About
              </a>
            </li>
            <li className="nav__item">
              <a className="nav__link" href="#section--3">
                Testimonials
              </a>
            </li>
            <li className="nav__item">
              <a className="nav__link" href="#section--4">
                Contact
              </a>
            </li>
            <li className="nav__item">
              <button
                className="nav__link nav__link--btn btn--show-modal"
                id="login__btn"
                onClick={openLoginModal}
              >
                Login
              </button>
            </li>
          </ul>
        </nav>

        <div className="header__title">
          <h1>
            <span className="highlight">Health Solution</span>
            For Better Tomorrow
          </h1>
          <h4>Streamlined Health Management for a Healthier Future.</h4>
          <button
            className="btn--book btn--scroll-to"
            onClick={openBookingModal}
          >
            Book An Appointment
          </button>
          <img
            src="/img/collage.png"
            className="header__img"
            alt="quickcredit loan items"
          />
        </div>
      </header>

      <section className="section" id="section--1">
        <div className="section__title">
          <h2 className="section__description">Features</h2>
          <h3 className="section__header">
            Find everything you need in a healthcare management system
          </h3>
        </div>

        <div className="features">
          <img
            src="img/image4.gif"
            data-src="img/image4.gif"
            alt="Computer"
            className="features__img lazy-img"
          />
          <div className="features__feature">
            <div className="features__icon">
              <svg>
                <use xlinkHref="img/icons.svg#icon-monitor"></use>
              </svg>
            </div>
            <h5 className="features__header">Efficient Patient Registration</h5>
            <p>
              Seamlessly register new patients and manage their information with
              ease. Accessible by doctors and authorized staff, our system
              ensures a streamlined onboarding process.
            </p>
          </div>

          <div className="features__feature">
            <div className="features__icon">
              <svg>
                <use xlinkHref="img/icons.svg#icon-trending-up"></use>
              </svg>
            </div>
            <h5 className="features__header">
              Automated Appointment Scheduling
            </h5>
            <p>
              Book, manage, and track appointments effortlessly. Our intelligent
              scheduling system helps avoid conflicts and ensures optimal time
              management for both patients and healthcare providers.
            </p>
          </div>
          <img
            src="img/image6.gif"
            data-src="img/image6.gif"
            alt="Plant"
            className="features__img lazy-img"
          />

          <img
            src="img/image1.gif"
            data-src="img/image1.gif"
            alt="Credit card"
            className="features__img lazy-img"
          />
          <div className="features__feature">
            <div className="features__icon">
              <svg>
                <use xlinkHref="img/icons.svg#icon-credit-card"></use>
              </svg>
            </div>
            <h5 className="features__header">Clinic Management</h5>
            <p>
              Maintain and update clinic details including doctor profiles,
              clinic timings, specialties, and facilities. Only authorized
              doctors can make changes, ensuring the information remains
              accurate and up-to-date.
            </p>
          </div>
        </div>
      </section>
      {/* 
      <section className="section" id="section--2">
        <div className="section__title">
          <h2 className="section__description">Operations</h2>
          <h3 className="section__header">
            Everything as simple as possible, but no simpler.
          </h3>
        </div>

        <div className="operations">
          <div className="operations__tab-container">
            <button
              className="btn operations__tab operations__tab--1 operations__tab--active"
              data-tab="1"
            >
              <span>01</span>Instant Apply
            </button>
            <button
              className="btn operations__tab operations__tab--2"
              data-tab="2"
            >
              <span>02</span>Instant Approval
            </button>
            <button
              className="btn operations__tab operations__tab--3"
              data-tab="3"
            >
              <span>03</span>Instant Payment
            </button>
          </div>
          <div className="operations__content operations__content--1 operations__content--active">
            <div className="operations__icon operations__icon--1">
              <svg>
                <use xlinkHref="img/icons.svg#icon-upload"></use>
              </svg>
            </div>
            <h5 className="operations__header">Quick and Hassle-Free</h5>
            <p>
              Say goodbye to endless paperwork. Our user-friendly platform
              requires minimal documentation, making the application process
              swift and straightforward.
            </p>
          </div>

          <div className="operations__content operations__content--2">
            <div className="operations__icon operations__icon--2">
              <svg>
                <use xlinkHref="img/icons.svg#icon-home"></use>
              </svg>
            </div>
            <h5 className="operations__header">Tailored to Fit Your Needs</h5>
            <p>
              Take control of your finances without the fear of additional fees.
              Our flexible terms empower you to repay your loan ahead of
              schedule without any penalties, helping you save on interest.
            </p>
          </div>
          <div className="operations__content operations__content--3">
            <div className="operations__icon operations__icon--3">
              <svg>
                <use xlinkHref="img/icons.svg#icon-user-x"></use>
              </svg>
            </div>
            <h5 className="operations__header">Your Trust, Our Priority</h5>
            <p>
              We believe in transparency every step of the way. Our terms and
              conditions are clear and easy to understand, ensuring that you are
              fully informed before committing to a loan.
            </p>
          </div>
        </div>
      </section> */}

      <section className="section" id="section--3">
        <div className="section__title section__title--testimonials">
          <h2 className="section__description">Not sure yet?</h2>
          <h3 className="section__header">
            Millions of Doctors are already making their lives simpler.
          </h3>
        </div>

        <div className="slider">
          <div className="slide slide--1">
            <div className="testimonial">
              <h5 className="testimonial__header">
                Best health care management ever!
              </h5>
              <blockquote className="testimonial__text">
                Lorem ipsum dolor sit, amet consectetur adipisicing elit.
                Accusantium quas quisquam non? Quas voluptate nulla minima
                deleniti optio ullam nesciunt, numquam corporis et asperiores
                laboriosam sunt, praesentium suscipit blanditiis. Necessitatibus
                id alias reiciendis, perferendis facere pariatur dolore veniam
                autem esse non voluptatem saepe provident nihil molestiae.
              </blockquote>
              <address className="testimonial__author">
                <img
                  src="img/user-1.jpg"
                  alt=""
                  className="testimonial__photo"
                />
                <h6 className="testimonial__name">Aarav Lynn</h6>
                <p className="testimonial__location">San Francisco, USA</p>
              </address>
            </div>
          </div>

          <div className="slide slide--2">
            <div className="testimonial">
              <h5 className="testimonial__header">
                Trusted by Healthcare Providers
              </h5>
              <blockquote className="testimonial__text">
                Quisquam itaque deserunt ullam, quia ea repellendus provident,
                ducimus neque ipsam modi voluptatibus doloremque, corrupti
                laborum. Incidunt numquam perferendis veritatis neque
                repellendus. Lorem, ipsum dolor sit amet consectetur adipisicing
                elit. Illo deserunt exercitationem deleniti.
              </blockquote>
              <address className="testimonial__author">
                <img
                  src="img/user-2.jpg"
                  alt=""
                  className="testimonial__photo"
                />
                <h6 className="testimonial__name">Miyah Miles</h6>
                <p className="testimonial__location">London, UK</p>
              </address>
            </div>
          </div>

          <div className="slide slide--3">
            <div className="testimonial">
              <h5 className="testimonial__header">
                Success Stories with Dampson HMS
              </h5>
              <blockquote className="testimonial__text">
                Debitis, nihil sit minus suscipit magni aperiam vel tenetur
                incidunt commodi architecto numquam omnis nulla autem,
                necessitatibus blanditiis modi similique quidem. Odio aliquam
                culpa dicta beatae quod maiores ipsa minus consequatur error
                sunt, deleniti saepe aliquid quos inventore sequi.
                Necessitatibus id alias reiciendis, perferendis facere.
              </blockquote>
              <address className="testimonial__author">
                <img
                  src="img/user-3.jpg"
                  alt=""
                  className="testimonial__photo"
                />
                <h6 className="testimonial__name">Francisco Gomes</h6>
                <p className="testimonial__location">Lisbon, Portugal</p>
              </address>
            </div>
          </div>

          <button className="slider__btn slider__btn--left">&larr;</button>
          <button className="slider__btn slider__btn--right">&rarr;</button>
          <div className="dots"></div>
        </div>
      </section>

      <section className="section section--sign-up">
        <div className="section__title">
          <h3 className="section__header">
            Experience the future of healthcare management with Dampson HMS.
          </h3>
        </div>
        <button className="btn btn--show-modal" onClick={openBookingModal}>
          Patient Booking
        </button>
      </section>

      <footer className="footer">
        <ul className="footer__nav">
          <li className="footer__item">
            <a className="footer__link" href="#">
              About
            </a>
          </li>
          <li className="footer__item">
            <a className="footer__link" href="#">
              Pricing
            </a>
          </li>
          <li className="footer__item">
            <a className="footer__link" href="#">
              Terms of Use
            </a>
          </li>
          <li className="footer__item">
            <a className="footer__link" href="#">
              Privacy Policy
            </a>
          </li>
          <li className="footer__item">
            <a className="footer__link" href="#">
              Careers
            </a>
          </li>
          <li className="footer__item">
            <a className="footer__link" href="#">
              Blog
            </a>
          </li>
          <li className="footer__item">
            <a className="footer__link" href="#">
              Contact Us
            </a>
          </li>
        </ul>
        <img src="/img/Dampson (5).png" alt="Logo" className="footer__logo" />
        <p className="footer__copyright">
          &copy; Copyright by{" "}
          <a className="footer__link twitter-link" target="_blank" href="">
            Dampson HMS
          </a>{" "}
          2024. All Rights Reserved. Built by Code Mavericks
        </p>
      </footer>

      <Modal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)}>
        {modalContent}
      </Modal>
    </div>
  );
};

export default LandingPage;
