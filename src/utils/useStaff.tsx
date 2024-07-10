import { useSession } from "next-auth/react";
import { useEffect, useState } from "react";
import axios from "axios";

export default function useAuth() {
  const API_URL = process.env.NEXT_PUBLIC_API_URL;
  const { data: session } = useSession();
  const [isStaff, setIsStaff] = useState(false);
  const [roles, setRoles] = useState([
    {
      id: 0,
      name: ""
    }
  ]);

  useEffect(() => {
    axios.get(API_URL + `/profiles/me`,
      {
        headers: {
          Authorization: `Bearer ${session?.accessToken}`
        }
      }
    )
      .then(response => {
        setRoles(response.data.roles);
      })
      .catch(error => {
        console.error(error);
      });
  });

  useEffect(() => {
    if (session === null) {
      setIsStaff(false);
    } else if (session !== undefined) {
      const isStaff = roles.some(r => r.name === "ROLE_MODERATOR" ||
        r.name === "ROLE_ADMIN");
      setIsStaff(isStaff);
    }
  }, [roles, session]);

  return isStaff;
}
