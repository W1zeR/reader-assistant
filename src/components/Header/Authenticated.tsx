import Link from "next/link";
import { ArrowRightStartOnRectangleIcon, ChevronDownIcon, Cog6ToothIcon, UserIcon } from "@heroicons/react/24/outline";
import { signOut, useSession } from "next-auth/react";
import { useEffect, useState } from "react";
import axios from "axios";
import { browserName, isMobile } from "react-device-detect";

const Authenticated = () => {
  const { data: session } = useSession();
  const [login, setLogin] = useState("");
  const API_URL = process.env.NEXT_PUBLIC_API_URL;

  useEffect(() => {
    axios.get(API_URL + "/profiles/me",
      {
        headers: {
          Authorization: `Bearer ${session?.accessToken}`
        }
      }
    )
      .then(response => {
        setLogin(response.data.login);
      })
      .catch(error => {
        console.error(error);
      });
  });

  return (
    <>
      <ul className="block lg:flex lg:space-x-12">
        <li key={1} className="group relative">
          <div className="ease-in-up hiddenpx-8 py-3 text-base font-medium text-dark transition duration-300
          hover:bg-opacity-90 md:block md:px-9 lg:px-6 xl:px-9 dark:text-white">
            <UserIcon className="h-6 w-6 inline-block dark:text-white" /> {login} <ChevronDownIcon
            className="h-4 w-4 inline-block dark:text-white" />
          </div>
          <>
            <div
              className={`submenu relative left-0 top-full rounded-sm bg-white transition-[top] duration-300 
                        group-hover:opacity-100 dark:bg-dark lg:invisible lg:absolute lg:top-[110%] lg:block 
                        lg:w-[250px] lg:p-4 lg:opacity-0 lg:shadow-lg lg:group-hover:visible lg:group-hover:top-full`}>
              <Link
                href="/settings"
                key={1}
                className="block rounded py-2.5 text-sm text-dark hover:text-primary dark:text-white/70
                          dark:hover:text-white lg:px-3"
              >
                <Cog6ToothIcon className="h-6 w-6 inline-block" /> Настройки
              </Link>
              <form
                action={async () => {
                  await signOut();
                  const deviceInfo = {
                    browserName: browserName,
                    deviceType: isMobile ? "mobile" : "desktop"
                  };
                  await axios.post(API_URL + "/profiles/logout", {
                    deviceInfo: deviceInfo,
                    token: session.accessToken
                  });
                }}
              >
                <button
                  className="block rounded py-2.5 text-sm text-dark hover:text-primary dark:text-white/70
                  dark:hover:text-white lg:px-3">
                  <ArrowRightStartOnRectangleIcon className="h-6 w-6 inline-block" /> Выйти
                </button>
              </form>
            </div>
          </>
        </li>
      </ul>

    </>
  );
};

export default Authenticated;
