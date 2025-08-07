import React from 'react'
import Hero from '../components/Hero.jsx'
import Featured from '../components/FeaturedSection.jsx'
import Banner from '../components/Banner.jsx'
import Testimonial from '../components/Testimonial.jsx'
import Newsletter from '../components/Newsletter.jsx'

const Home = () => {
  return (
    <>
      <Hero />
      <Featured />
      <Banner />
      <Testimonial />
      <Newsletter />
    </>
  )
}

export default Home
