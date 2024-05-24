import { useSession } from "next-auth/react";
import { useEffect, useState } from "react";
import { HeartIcon } from "@heroicons/react/24/outline";
import axios from "axios";
import { HeartIcon as HeartIconSolid } from "@heroicons/react/24/solid";

const LikeButton = ({ likes, quoteId }) => {
  const { data: session } = useSession();
  const [liked, setLiked] = useState(false);
  const API_URL = process.env.NEXT_PUBLIC_API_URL;

  useEffect(() => {
    axios.get(API_URL + `/quotes/${quoteId}/likeStatus`,
      {
        headers: {
          Authorization: `Bearer ${session.accessToken}`
        }
      }
    )
      .then(response => {
        setLiked(response.data);
      })
      .catch(error => {
        console.error(error);
      });
  });

  const handleLike = async () => {
    if (!liked) {
      await axios.put(API_URL + `/quotes/${quoteId}/whoLiked/${session.userId}`, null,
        {
          headers: {
            Authorization: `Bearer ${session.accessToken}`
          }
        });
    } else {
      await axios.delete(API_URL + `/quotes/${quoteId}/whoLiked/${session.userId}`,
        {
          headers: {
            Authorization: `Bearer ${session.accessToken}`
          }
        });
    }
  };

  return (
    <button onClick={handleLike}>
      {liked ? <button
          className="bg-red-500 hover:bg-red-700 dark:bg-red-700 dark:hover:bg-red-900 text-white
                font-bold py-2 px-4 rounded-full text-base stroke-red-500 hover:stroke-red-700
                dark:stroke-red-700 dark:hover:stroke-red-900">
          <HeartIconSolid className="h-6 w-6 inline-block" /> {likes}
        </button> :
        <button
          className="bg-blue-500 hover:bg-blue-700 dark:bg-blue-700 dark:hover:bg-blue-900 text-white
                font-bold py-2 px-4 rounded-full text-base">
          <HeartIcon className="h-6 w-6 inline-block" /> {likes}
        </button>}
    </button>
  );
};

export default LikeButton;