import React, { useEffect } from 'react'
import NavbarOwner from '../../components/owner/NavbarOwner.jsx'
import Sidebar from '../../components/owner/Sidebar.jsx'
import { Outlet } from 'react-router-dom'
import { useAppContext } from '../../context/AppContext.jsx'

const Layout = () => {

  const { isOwner, navigate } = useAppContext();
  
  //Check user is owner,not navigate to home page.
  useEffect(()=>{
    if(!isOwner){
      navigate('/')
    }
  },[isOwner])

  return (
    <div className='flex flex-col'>
      <NavbarOwner />
      <div className='flex'>
        <Sidebar />
        <Outlet />
      </div>
    </div>
  )
}

export default Layout
