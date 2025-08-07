import React, { useEffect, useState } from 'react'
import Title from '../../components/owner/Title.jsx'
import { useAppContext } from '../../context/AppContext.jsx'
import toast from 'react-hot-toast'

const ManageBookings = () => {

  const { currency, axios } = useAppContext()

  const [bookings, setBookings] = useState([]);

  const fetchOwnerBookings = async () => {
    try {
      const { data } = await axios.get('/api/bookings/owner')
      if (data.success) {
        setBookings(data.bookings);
      } else {
        toast.error(data.message)
      }
    } catch (error) {
      toast.error(error.message)
    }
  }

  const changeBookingStatus = async(booking, status) => {
    try {
      const { data } = await axios.patch('/api/bookings/change-status',{id:booking.id, status:status.toUpperCase()})
      if(data.success){
        toast.success(data.message)
        fetchOwnerBookings()
      }else{
        toast.error(data.message)
      }
    } catch (error) {
      toast.error(error.message)
    }
  }

  useEffect(() => {
    fetchOwnerBookings();
  }, [])
  return (
    <div className='p-4 pt-10 md:px-10 w-full'>
      <Title title='Manage Bookings' subTitle='Track all customer bookings, approve or cancel requests, and manage booking statuses.' />

      <div className='max-w-3xl w-full rounded-md overflow-hidden border border-borderColor mt-6'>
        <table className='w-full border-collapse text-left text-sm text-gray-600' >
          <thead className='text-gray-500'>
            <tr>
              <th className='p-3 font-medium'>Car</th>
              <th className='p-3 font-medium max-md:hidden' >	Date Range</th>
              <th className='p-3 font-medium'>Total</th>
              <th className='p-3 font-medium max-md:hidden'>Status</th>
              <th className='p-3 font-medium'>Actions</th>
            </tr>
          </thead>
          <tbody>
            {bookings.map((booking, index) => (
              <tr key={index} className='border-t border-borderColor text-'>
                <td className='p-3 flex items-center gap-3'>
                  <img src={booking.car.image} alt="" className='h-12 w-12 aspect-square rounded-md object-cover' />
                  <p className='font-medium max-md:hidden'>{booking.car.brand} {booking.car.model}</p>
                </td>
                <td className='p-3 max-md:hidden'>
                  {booking.pickupDate} To {booking.returnDate}
                </td>
                <td className='p-3'>{currency}{booking.price}</td>
                <td className='p-3 max-md:hidden'>
                  <div className={`rounded-full text-center px-3 py-1 border border-borderColor ${booking.status === 'confirmed' && 'text-green-500 bg-green-100'} ${booking.status === 'completed' && 'text-blue-500 bg-blue-100'} ${booking.status === 'pending' && 'text-red-500 bg-red-100'}`}>{booking.status}</div>
                </td>
                <td className='p-3'>
                  {
                    booking.status.toLowerCase() === 'pending' ? (
                      <select value={booking.status} onChange={(e)=>changeBookingStatus(booking,e.target.value)} className='px-2 py-1.5 mt-1 text-gray-500 border border-borderColor rounded-md outline-none'>
                        <option value="pending">Pending</option>
                        <option value="cancelled">Cancelled</option>
                        <option value="confirmed">Confirmed</option>
                      </select>
                    ) : (
                      <span className={`px-3 py-1 rounded-full text-xs font-semibold ${booking.status.toLowerCase() === 'confirmed' ? 'bg-green-100 text-green-500 ' : 'bg-red-100 text-red-500'}`}>{booking.status}</span>
                    )
                  }
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  )
}

export default ManageBookings
